package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.ITransAbsorbActivityController;
import cj.netos.oc.wallet.bo.TransAbsorbBO;
import cj.netos.oc.wallet.result.TransAbsorbResult;
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
@CjService(name = "transAbsorbActivityController")
public class TransAbsorbActivityController implements ITransAbsorbActivityController {
    @CjServiceRef(refByName = "@.rabbitmq.producer.ack")
    IRabbitMQProducer rabbitMQProducer;

    @CjServiceRef
    ISettleTradeService settleTradeService;
    @CjTransaction
    @Override
    public void doReceipt(TransAbsorbBO transAbsorbBO) throws CircuitException {
        settleTradeService.transAbsorb(transAbsorbBO);
    }
    @CjTransaction
    @Override
    public void sendReceiptAck(TransAbsorbResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "transAbsorb");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        rabbitMQProducer.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
