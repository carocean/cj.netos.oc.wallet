package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IPayActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.PayBO;
import cj.netos.oc.wallet.result.PayResult;
import cj.netos.rabbitmq.IRabbitMQProducer;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;

import java.util.HashMap;

@CjService(name = "payActivityController")
public class PayActivityController implements IPayActivityController {
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_payTrade")
    IRabbitMQProducer toGateway_ack_receipt_payTrade;

    @CjServiceRef
    ISettleTradeService settleTradeService;

    @Override
    public void doReceipt(PayBO payBO) throws CircuitException {
        settleTradeService.payTrade(payBO);
    }

    @Override
    public void sendReceiptAck(PayResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "payTrade");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_payTrade.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
