package cj.netos.oc.wallet.cmd;

import cj.netos.oc.wallet.IPurchaseActivityController;
import cj.netos.oc.wallet.bo.PurchaseBO;
import cj.netos.oc.wallet.bo.PurchasedBO;
import cj.netos.oc.wallet.program.ICuratorPathChecker;
import cj.netos.oc.wallet.result.PurchaseResult;
import cj.netos.oc.wallet.result.PurchasedResult;
import cj.netos.oc.wallet.result.WithdrawResult;
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

@CjConsumer(name = "trade")
@CjService(name = "/trade/settle.mhub#purchase")
public class PurchaseSettleCommand implements IConsumerCommand {

    @CjServiceRef
    IPurchaseActivityController purchaseActivityController;

    @CjServiceRef(refByName = "curator.framework")
    CuratorFramework framework;

    @CjServiceRef
    ICuratorPathChecker curatorPathChecker;


    @Override
    public void command(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws RabbitMQException, RetryCommandException {
        LongString personLS = (LongString) properties.getHeaders().get("person");
        String path = String.format("/wallet/%s/locks", personLS.toString());
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new RabbitMQException("500", e);
        }
        PurchasedBO purchasedBO = new Gson().fromJson(new String(body), PurchasedBO.class);
        PurchaseResult result = new PurchaseResult("200","OK");
        result.setPerson(purchasedBO.getPerson());
        result.setSn(purchasedBO.getSn());

        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            mutex.acquire();
            purchaseActivityController.settle(purchasedBO);
            result.setRecord(new Gson().toJson(purchasedBO));
            purchaseActivityController.sendSettleAck(result);
        } catch (RabbitMQException e) {
            throw e;
        } catch (Exception e) {
            CircuitException ce = CircuitException.search(e);
            if (ce != null) {
                result.setStatus(ce.getStatus());
                result.setMessage(ce.getMessage());
                try {
                    purchaseActivityController.sendSettleAck(result);
                } catch (CircuitException ex) {
                    CJSystem.logging().error(getClass(), ex);
                }
                throw new RabbitMQException(ce.getStatus(), ce);
            }
            result.setStatus("500");
            result.setMessage(e.getMessage());
            try {
                purchaseActivityController.sendSettleAck(result);
            } catch (CircuitException ex) {
                CJSystem.logging().error(getClass(), ex);
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
