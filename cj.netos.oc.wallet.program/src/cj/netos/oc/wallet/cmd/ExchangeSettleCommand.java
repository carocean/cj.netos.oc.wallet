package cj.netos.oc.wallet.cmd;

import cj.netos.oc.wallet.IExchangeActivityController;
import cj.netos.oc.wallet.bo.ExchangeBO;
import cj.netos.oc.wallet.bo.ExchangedBO;
import cj.netos.oc.wallet.program.ICuratorPathChecker;
import cj.netos.oc.wallet.result.ExchangeResult;
import cj.netos.oc.wallet.result.ExchangingResult;
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
@CjService(name = "/trade/settle.mhub#exchange")
public class ExchangeSettleCommand implements IConsumerCommand {
    @CjServiceRef
    IExchangeActivityController exchangeActivityController;
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

        ExchangedBO bo = new Gson().fromJson(new String(body), ExchangedBO.class);
        ExchangingResult result = new ExchangingResult("200", "OK");
        result.setPerson(bo.getExchanger());
        result.setSn(bo.getSn());

        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            mutex.acquire();
            ExchangeResult result1 = exchangeActivityController.settle(bo);
            result.setRecord(new Gson().toJson(result1));
            exchangeActivityController.sendSettleAck(result);
        } catch (RabbitMQException e) {
            throw e;
        } catch (Exception e) {
            CircuitException ce = CircuitException.search(e);
            if (ce != null) {
                try {
                    result.setStatus(ce.getStatus());
                    result.setMessage(ce.getMessage());
                    exchangeActivityController.sendSettleAck(result);
                } catch (CircuitException ex) {
                    CJSystem.logging().error(getClass(), ex);
                }
                throw new RabbitMQException(ce.getStatus(), ce);
            }
            try {
                result.setStatus("500");
                result.setMessage(e.getMessage());
                exchangeActivityController.sendSettleAck(result);
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
