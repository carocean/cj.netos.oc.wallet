package cj.netos.oc.wallet.cmd;

import cj.netos.oc.wallet.ITransShunterActivityController;
import cj.netos.oc.wallet.bo.WithdrawShunterBO;
import cj.netos.oc.wallet.program.ICuratorPathChecker;
import cj.netos.oc.wallet.result.TransShuntResult;
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

@CjConsumer(name = "fromGateway_settle_transShunter")
@CjService(name = "/trade/settle.mhub#transShunter")
public class TransShunterSettleCommand implements IConsumerCommand {

    @CjServiceRef
    ITransShunterActivityController transShunterActivityController;

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
        WithdrawShunterBO transShuntedBO = new Gson().fromJson(new String(body), WithdrawShunterBO.class);
        TransShuntResult result = new TransShuntResult("200","OK");
        result.setPerson(transShuntedBO.getPerson());
        result.setSn(transShuntedBO.getSn());

        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            mutex.acquire();
            transShunterActivityController.settle(transShuntedBO);
            result.setRecord(new Gson().toJson(transShuntedBO));
            transShunterActivityController.sendSettleAck(result);
        } catch (RabbitMQException e) {
            throw e;
        } catch (Exception e) {
            CircuitException ce = CircuitException.search(e);
            if (ce != null) {
                result.setStatus(ce.getStatus());
                result.setMessage(ce.getMessage());
                try {
                    transShunterActivityController.sendSettleAck(result);
                } catch (CircuitException ex) {
                    CJSystem.logging().error(getClass(), ex);
                }
                throw new RabbitMQException(ce.getStatus(), ce);
            }
            result.setStatus("500");
            result.setMessage(e.getMessage());
            try {
                transShunterActivityController.sendSettleAck(result);
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
