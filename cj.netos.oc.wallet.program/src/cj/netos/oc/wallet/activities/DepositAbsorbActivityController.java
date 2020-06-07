package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IDepositAbsorbActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.DepositAbsorbBO;
import cj.netos.oc.wallet.result.DepositAbsorbResult;
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
@CjService(name = "depositAbsorbActivityController")
public class DepositAbsorbActivityController implements IDepositAbsorbActivityController {
    @CjServiceRef(refByName = "@.rabbitmq.producer.ack")
    IRabbitMQProducer rabbitMQProducer;

    @CjServiceRef
    ISettleTradeService settleTradeService;


    @CjTransaction
    @Override
    public void doReceipt(DepositAbsorbBO depositAbsorbBO) throws CircuitException {
        //直接存入洇金
        settleTradeService.depositAbsorb(depositAbsorbBO);
    }

    @CjTransaction
    @Override
    public void sendReceiptAck(DepositAbsorbResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "depositAbsorb");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        rabbitMQProducer.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
