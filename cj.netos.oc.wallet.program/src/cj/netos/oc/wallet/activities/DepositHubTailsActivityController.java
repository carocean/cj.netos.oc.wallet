package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IDepositHubTailsActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.DepositHubTailsBO;
import cj.netos.oc.wallet.result.DepositHubTailsResult;
import cj.netos.rabbitmq.IRabbitMQProducer;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;

import java.util.HashMap;

@CjService(name = "depositHubTailsActivityController")
public class DepositHubTailsActivityController implements IDepositHubTailsActivityController {
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_depositHubTails")
    IRabbitMQProducer toGateway_ack_receipt_depositHubTails;

    @CjServiceRef
    ISettleTradeService settleTradeService;

    @Override
    public void doReceipt(DepositHubTailsBO depositAbsorbBO) throws CircuitException {
        //直接存入洇金
        settleTradeService.depositHubTails(depositAbsorbBO);
    }

    @Override
    public void sendReceiptAck(DepositHubTailsResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "depositHubTails");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_depositHubTails.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
