package cj.netos.oc.wallet.cmd;

import cj.netos.oc.wallet.IPayActivityController;
import cj.netos.oc.wallet.ITransProfitActivityController;
import cj.netos.oc.wallet.bo.PayBO;
import cj.netos.oc.wallet.bo.TransProfitBO;
import cj.netos.oc.wallet.program.ICuratorPathChecker;
import cj.netos.oc.wallet.result.PayResult;
import cj.netos.oc.wallet.result.TransProfitResult;
import cj.netos.rabbitmq.CjConsumer;
import cj.netos.rabbitmq.RabbitMQException;
import cj.netos.rabbitmq.RetryCommandException;
import cj.netos.rabbitmq.consumer.IConsumerCommand;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.annotation.CjServiceSite;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.LongString;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

@CjConsumer(name = "trade")
@CjService(name = "/trade/receipt.mhub#fromGateway_receipt_payTrade")
public class PayTradeReceiptCommand implements IConsumerCommand {
    @CjServiceSite
    IServiceSite site;

    @CjServiceRef(refByName = "curator.framework")
    CuratorFramework framework;

    @CjServiceRef
    ICuratorPathChecker curatorPathChecker;

    @CjServiceRef
    IPayActivityController payActivityController;

    @Override
    public void command(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws RabbitMQException, RetryCommandException {
        LongString personLS = (LongString) properties.getHeaders().get("person");
        String path = String.format("/wallet/%s/locks", personLS.toString());
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new RabbitMQException("500", e);
        }
        PayBO payBO = new Gson().fromJson(new String(body), PayBO.class);
        PayResult result = new PayResult("200", "ok");
        result.setPerson(payBO.getPerson());
        result.setSn(payBO.getSn());
        result.setRecord(new Gson().toJson(payBO));

        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            mutex.acquire();
            payActivityController.doReceipt(payBO);
            payActivityController.sendReceiptAck(result);
        } catch (RabbitMQException e) {
            throw e;
        } catch (Exception e) {
            CircuitException ce = CircuitException.search(e);
            if (ce != null) {
                result.setStatus(ce.getStatus());
                result.setMessage(ce.getMessage());
                try {
                    payActivityController.sendReceiptAck(result);
                } catch (CircuitException e1) {
                    CJSystem.logging().error(getClass(), e1);
                }
                throw new RabbitMQException(ce.getStatus(), ce);
            }
            result.setStatus("500");
            result.setMessage(e.getMessage());
            try {
                payActivityController.sendReceiptAck(result);
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
