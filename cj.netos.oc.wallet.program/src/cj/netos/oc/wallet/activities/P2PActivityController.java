package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IP2PActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.P2PBO;
import cj.netos.oc.wallet.result.P2PResult;
import cj.netos.rabbitmq.IRabbitMQProducer;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;

import java.util.HashMap;

@CjService(name = "p2pActivityController")
public class P2PActivityController implements IP2PActivityController {
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_p2p")
    IRabbitMQProducer toGateway_ack_receipt_p2p;

    @CjServiceRef
    ISettleTradeService settleTradeService;

    @Override
    public void doReceipt(P2PBO pbo) throws CircuitException {
        settleTradeService.p2p(pbo);
    }

    @Override
    public void sendReceiptAck(P2PResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "p2p");
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_p2p.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
