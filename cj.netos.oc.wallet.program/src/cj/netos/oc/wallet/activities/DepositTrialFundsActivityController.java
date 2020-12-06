package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IDepositTrialFundsActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.DepositTrialBO;
import cj.netos.oc.wallet.result.DepositAbsorbResult;
import cj.netos.oc.wallet.result.DepositTrialFundsResult;
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
@CjService(name = "depositTrialFundsActivityController")
public class DepositTrialFundsActivityController implements IDepositTrialFundsActivityController {

    @CjServiceRef
    ISettleTradeService settleTradeService;
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_depositTrialFunds")
    IRabbitMQProducer toGateway_ack_receipt_depositTrialFunds;

    @CjTransaction
    @Override
    public void doReceipt(DepositTrialBO depositTrialBO) {
        //直接存入体验金
        settleTradeService.depositTrialFunds(depositTrialBO);
    }

    @CjTransaction
    @Override
    public void sendReceiptAck(DepositTrialFundsResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "depositTrialFunds");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_depositTrialFunds.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
