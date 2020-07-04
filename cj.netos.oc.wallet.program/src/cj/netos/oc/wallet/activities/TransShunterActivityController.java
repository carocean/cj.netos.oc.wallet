package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IOnorderService;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.ITransShunterActivityController;
import cj.netos.oc.wallet.bo.TransShuntBO;
import cj.netos.oc.wallet.bo.WithdrawShunterBO;
import cj.netos.oc.wallet.result.PurchasingResult;
import cj.netos.oc.wallet.result.TransShuntResult;
import cj.netos.oc.wallet.result.WithdrawShunterResult;
import cj.netos.rabbitmq.IRabbitMQProducer;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.annotation.CjServiceSite;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.util.Encript;
import cj.studio.orm.mybatis.annotation.CjTransaction;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CjBridge(aspects = "@transaction")
@CjService(name = "transShunterActivityController")
public class TransShunterActivityController implements ITransShunterActivityController {

    @CjServiceSite
    IServiceSite site;
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_transShunter")
    IRabbitMQProducer toGateway_ack_receipt_transShunter;
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_settle_transShunter")
    IRabbitMQProducer toGateway_ack_settle_transShunter;
    @CjServiceRef
    ISettleTradeService settleTradeService;

    @CjTransaction
    @Override
    public WithdrawShunterResult receipt(TransShuntBO bo) throws CircuitException {
        return call_wenybank_withdraw(bo);
    }

    @CjTransaction
    @Override
    public void sendReceiptAck(TransShuntResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "transShunter");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_transShunter.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }

    @CjTransaction
    @Override
    public void settle(WithdrawShunterBO transShuntedBO) throws CircuitException {
        settleTradeService.transShunt(transShuntedBO);
    }

    @CjTransaction
    @Override
    public void sendSettleAck(TransShuntResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/settle/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "transShunter");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_settle_transShunter.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }

    private WithdrawShunterResult call_wenybank_withdraw(TransShuntBO bo) throws CircuitException {
        OkHttpClient client = (OkHttpClient) site.getService("@.http");

        String appid = site.getProperty("appid");
        String appKey = site.getProperty("appKey");
        String appSecret = site.getProperty("appSecret");
        String nonce = Encript.md5(String.format("%s%s", UUID.randomUUID().toString(), System.currentTimeMillis()));
        String sign = Encript.md5(String.format("%s%s%s", appKey, nonce, appSecret));

        String portsUrl = site.getProperty("rhub.ports.wybank.trade");
        String url = String.format("%s" +
                        "?wenyBankID=%s" +
                        "&shunter=%s" +
                        "&withdrawer=%s" +
                        "&withdrawerName=%s" +
                        "&req_amount=%s" +
                        "&out_trade_sn=%s" +
                        "&note=%s",
                portsUrl,
                bo.getWenyBankID(),
                bo.getShunter(),
                bo.getPerson(),
                bo.getPersonName(),
                bo.getDemandAmount(),
                bo.getSn(),
                bo.getNote()
        );

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Rest-Command", "withdraw")
                .addHeader("app-id", appid)
                .addHeader("app-key", appKey)
                .addHeader("app-nonce", nonce)
                .addHeader("app-sign", sign)
                .get()
                .build();
        final Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new CircuitException("1002", e);
        }
        if (response.code() >= 400) {
            throw new CircuitException("1002", String.format("远程访问失败:%s", response.message()));
        }
        String json = null;
        try {
            json = response.body().string();
        } catch (IOException e) {
            throw new CircuitException("1002", e);
        }
        Map<String, Object> map = new Gson().fromJson(json, HashMap.class);
        if (Double.parseDouble(map.get("status") + "") >= 400) {
            throw new CircuitException(map.get("status") + "", map.get("message") + "");
        }
        String v = (String) map.get("dataText");
        WithdrawShunterResult result = new Gson().fromJson(v, WithdrawShunterResult.class);
        return result;
    }

}
