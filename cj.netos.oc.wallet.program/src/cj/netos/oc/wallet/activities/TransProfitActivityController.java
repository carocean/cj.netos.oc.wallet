package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.ITransAbsorbActivityController;
import cj.netos.oc.wallet.ITransProfitActivityController;
import cj.netos.oc.wallet.bo.TransAbsorbBO;
import cj.netos.oc.wallet.bo.TransProfitBO;
import cj.netos.oc.wallet.result.TransAbsorbResult;
import cj.netos.oc.wallet.result.TransProfitResult;
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
@CjService(name = "transProfitActivityController")
public class TransProfitActivityController implements ITransProfitActivityController {
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_transProfit")
    IRabbitMQProducer toGateway_ack_receipt_transProfit;

    @CjServiceRef
    ISettleTradeService settleTradeService;
    @CjTransaction
    @Override
    public void doReceipt(TransProfitBO transAbsorbBO) throws CircuitException {
        settleTradeService.transProfit(transAbsorbBO);
    }
    @CjTransaction
    @Override
    public void sendReceiptAck(TransProfitResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "transProfit");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_transProfit.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
