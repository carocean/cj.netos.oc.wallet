package cj.netos.oc.wallet.cmd;

import cj.netos.oc.wallet.IPurchaseActivityController;
import cj.netos.oc.wallet.ITransShunterActivityController;
import cj.netos.oc.wallet.bo.PurchaseBO;
import cj.netos.oc.wallet.bo.TransShuntBO;
import cj.netos.oc.wallet.program.ICuratorPathChecker;
import cj.netos.oc.wallet.result.PurchaseResult;
import cj.netos.oc.wallet.result.PurchasingResult;
import cj.netos.oc.wallet.result.TransShuntResult;
import cj.netos.oc.wallet.result.WithdrawShunterResult;
import cj.netos.rabbitmq.CjConsumer;
import cj.netos.rabbitmq.RabbitMQException;
import cj.netos.rabbitmq.RetryCommandException;
import cj.netos.rabbitmq.consumer.IConsumerCommand;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.LongString;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.io.IOException;

@CjConsumer(name = "trade")
@CjService(name = "/trade/receipt.mhub#transShunter")
public class TransShunterReceiptCommand implements IConsumerCommand {
    @CjServiceRef
    ITransShunterActivityController transShunterActivityController;
    @CjServiceRef(refByName = "curator.framework")
    CuratorFramework framework;

    @CjServiceRef
    ICuratorPathChecker curatorPathChecker;


    @Override
    public void command(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws RabbitMQException, RetryCommandException, IOException {
        LongString personLS = (LongString) properties.getHeaders().get("person");
        String path = String.format("/wallet/%s/locks", personLS.toString());
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new RabbitMQException("500", e);
        }
        TransShuntBO transShuntBO = new Gson().fromJson(new String(body), TransShuntBO.class);
        TransShuntResult result = new TransShuntResult("200", "ok");
        result.setPerson(transShuntBO.getPerson());
        result.setSn(transShuntBO.getSn());

        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            mutex.acquire();
            WithdrawShunterResult bankResult = transShunterActivityController.receipt(transShuntBO);
            result.setRecord(new Gson().toJson(bankResult));
            result.setOutTradeSn(bankResult.getSn());
            transShunterActivityController.sendReceiptAck(result);
        } catch (RabbitMQException e) {
            throw e;
        } catch (Exception e) {
            CircuitException ce = CircuitException.search(e);
            if (ce != null) {
                result.setStatus(ce.getStatus());
                result.setMessage(ce.getMessage());
                try {
                    transShunterActivityController.sendReceiptAck(result);
                } catch (CircuitException e1) {
                    CJSystem.logging().error(getClass(), e1);
                }
                throw new RabbitMQException(ce.getStatus(), ce);
            }
            result.setStatus("500");
            result.setMessage(e.getMessage());
            try {
                transShunterActivityController.sendReceiptAck(result);
            } catch (CircuitException e1) {
                CJSystem.logging().error(getClass(), e1);
            }
            throw new RabbitMQException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
