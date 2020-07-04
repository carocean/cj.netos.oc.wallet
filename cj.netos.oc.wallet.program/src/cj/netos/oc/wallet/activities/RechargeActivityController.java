package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IRechargeActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.RechargeBO;
import cj.netos.oc.wallet.result.RechargeResult;
import cj.netos.rabbitmq.IRabbitMQProducer;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;

import java.util.HashMap;

@CjBridge(aspects = "@transaction")
@CjService(name = "rechargeActivityController")
public class RechargeActivityController implements IRechargeActivityController {

    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_settle.recharge")
    IRabbitMQProducer rabbitMQProducer;

    @CjServiceRef
    ISettleTradeService settleTradeService;

    @CjTransaction
    @Override
    public void settle(RechargeBO bo) throws CircuitException {
        settleTradeService.recharge(bo);
    }


    @CjTransaction
    @Override
    public void sendSettleAck(RechargeResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/settle/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "recharge");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        rabbitMQProducer.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
