package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IModuleTransinActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.ModuleTransinBO;
import cj.netos.oc.wallet.result.ModuleTransinResult;
import cj.netos.rabbitmq.IRabbitMQProducer;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;

import java.util.HashMap;

@CjService(name = "moduleTransinActivityController")
public class ModuleTransinActivityController implements IModuleTransinActivityController {
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_moduleTransin")
    IRabbitMQProducer toGateway_ack_receipt_moduleTransin;

    @CjServiceRef
    ISettleTradeService settleTradeService;
    @Override
    public void doReceipt(ModuleTransinBO moduleTransinBO) throws CircuitException {
        settleTradeService.moduleTransin(moduleTransinBO);
    }

    @Override
    public void sendReceiptAck(ModuleTransinResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "moduleTransin");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_moduleTransin.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
