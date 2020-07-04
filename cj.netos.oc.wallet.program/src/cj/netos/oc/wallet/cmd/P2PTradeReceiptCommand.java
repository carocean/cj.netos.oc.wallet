package cj.netos.oc.wallet.cmd;

import cj.netos.oc.wallet.IP2PActivityController;
import cj.netos.oc.wallet.IPayActivityController;
import cj.netos.oc.wallet.bo.P2PBO;
import cj.netos.oc.wallet.bo.PayBO;
import cj.netos.oc.wallet.program.ICuratorPathChecker;
import cj.netos.oc.wallet.result.P2PResult;
import cj.netos.oc.wallet.result.PayResult;
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

@CjConsumer(name = "fromGateway_receipt_p2p")
@CjService(name = "/trade/receipt.mhub#p2p")
public class P2PTradeReceiptCommand implements IConsumerCommand {
    @CjServiceSite
    IServiceSite site;

    @CjServiceRef(refByName = "curator.framework")
    CuratorFramework framework;

    @CjServiceRef
    ICuratorPathChecker curatorPathChecker;

    @CjServiceRef(refByName = "p2pActivityController")
    IP2PActivityController p2pActivityController;

    @Override
    public void command(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws RabbitMQException, RetryCommandException {
        LongString personLS = (LongString) properties.getHeaders().get("payer");
        String path = String.format("/wallet/%s/locks", personLS.toString());
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new RabbitMQException("500", e);
        }
        P2PBO pbo = new Gson().fromJson(new String(body), P2PBO.class);
        P2PResult result = new P2PResult("200", "ok");
        result.setPayer(pbo.getPayer());
        result.setPayee(pbo.getPayee());
        result.setSn(pbo.getSn());
        result.setRecord(new Gson().toJson(pbo));

        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            mutex.acquire();
            p2pActivityController.doReceipt(pbo);
            p2pActivityController.sendReceiptAck(result);
        } catch (RabbitMQException e) {
            throw e;
        } catch (Exception e) {
            CircuitException ce = CircuitException.search(e);
            if (ce != null) {
                result.setStatus(ce.getStatus());
                result.setMessage(ce.getMessage());
                try {
                    p2pActivityController.sendReceiptAck(result);
                } catch (CircuitException e1) {
                    CJSystem.logging().error(getClass(), e1);
                }
                throw new RabbitMQException(ce.getStatus(), ce);
            }
            result.setStatus("500");
            result.setMessage(e.getMessage());
            try {
                p2pActivityController.sendReceiptAck(result);
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
