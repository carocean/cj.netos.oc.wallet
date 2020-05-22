package cj.netos.oc.wallet.cmd;

import cj.netos.oc.wallet.IOnorderService;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.OnorderBO;
import cj.netos.oc.wallet.bo.WithdrawBO;
import cj.netos.oc.wallet.program.ICuratorPathChecker;
import cj.netos.oc.wallet.result.OnorderResult;
import cj.netos.oc.wallet.result.WithdrawResult;
import cj.netos.rabbitmq.CjConsumer;
import cj.netos.rabbitmq.IRabbitMQProducer;
import cj.netos.rabbitmq.RabbitMQException;
import cj.netos.rabbitmq.RetryCommandException;
import cj.netos.rabbitmq.consumer.IConsumerCommand;
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

import java.util.HashMap;

@CjConsumer(name = "trade")
@CjService(name = "/trade/onorder.mq#put")
public class PutOnorderCommand implements IConsumerCommand {
    @CjServiceSite
    IServiceSite site;

    @CjServiceRef(refByName = "curator.framework")
    CuratorFramework framework;

    @CjServiceRef
    ICuratorPathChecker curatorPathChecker;

    @CjServiceRef(refByName = "@.rabbitmq.producer.ack")
    IRabbitMQProducer rabbitMQProducer;

    @CjServiceRef
    IOnorderService onorderService;

    @Override
    public void command(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws RabbitMQException, RetryCommandException {
        LongString personLS = (LongString) properties.getHeaders().get("person");
        String path = String.format("/wallet/%s/locks", personLS.toString());
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new RabbitMQException("500", e);
        }
        OnorderBO onorderBO = new Gson().fromJson(new String(body), OnorderBO.class);
        String status = "200";
        String message = "OK";
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            mutex.acquire();
            onorderService.put(onorderBO);
        } catch (RabbitMQException e) {
            status = e.getStatus();
            message = e.getMessage();
            throw e;
        } catch (Exception e) {
            status = "500";
            message = e.getMessage();
            throw new RabbitMQException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            OnorderResult result = new OnorderResult();
            result.setSn(onorderBO.getRefsn());
            result.setPerson(onorderBO.getPerson());
            result.setMessage(message);
            result.setStatus(status);
            result.setRecord(onorderBO);
            try {
                ack(result);
            } catch (CircuitException e) {
                throw new RabbitMQException(e.getStatus(), e.getMessage());
            }
        }

    }

    private void ack(OnorderResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mq")
                .headers(new HashMap<String, Object>() {{
                    put("command", "withdraw");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        rabbitMQProducer.publish(properties, new Gson().toJson(result).getBytes());
    }

}
