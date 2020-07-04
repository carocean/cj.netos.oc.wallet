package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IOnorderService;
import cj.netos.oc.wallet.IPurchaseActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.OnorderBO;
import cj.netos.oc.wallet.bo.PurchaseBO;
import cj.netos.oc.wallet.bo.PurchasedBO;
import cj.netos.oc.wallet.result.PurchaseResult;
import cj.netos.oc.wallet.result.PurchasingResult;
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
@CjService(name = "purchaseActivityController")
public class PurchaseActivityController implements IPurchaseActivityController {
    @CjServiceRef
    IOnorderService onorderService;

    @CjServiceSite
    IServiceSite site;
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_purchase")
    IRabbitMQProducer toGateway_ack_receipt_purchase;
    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_settle_purchase")
    IRabbitMQProducer toGateway_ack_settle_purchase;
    @CjServiceRef
    ISettleTradeService settleTradeService;


    @CjTransaction
    @Override
    public PurchasingResult receipt(PurchaseBO bo) throws CircuitException {
        //1。预扣款
        //2。发起申购（银行）
        putOnorder(bo);
        PurchasingResult purchasingResult = call_wenybank_purchase(bo);
        return purchasingResult;
    }

    @CjTransaction
    @Override
    public void sendReceiptAck(PurchaseResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "purchase");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_purchase.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }

    private void putOnorder(PurchaseBO bo) throws CircuitException {
        OnorderBO onOrderBO = new OnorderBO();
        onOrderBO.setPersonName(bo.getPurchaserName());
        onOrderBO.setRefType("purchase");
        onOrderBO.setAmount(bo.getAmount());
        onOrderBO.setNote(bo.getNote());
        onOrderBO.setPerson(bo.getPurchaser());
        onOrderBO.setRefsn(bo.getSn());
        onOrderBO.setOrder(8);
        onOrderBO.setCause("申购纹银");
        onorderService.put(onOrderBO);
    }

    //发纹银银行发起承兑
    private PurchasingResult call_wenybank_purchase(PurchaseBO purchaseBO) throws CircuitException {

        OkHttpClient client = (OkHttpClient) site.getService("@.http");

        String appid = site.getProperty("appid");
        String appKey = site.getProperty("appKey");
        String appSecret = site.getProperty("appSecret");
        String nonce = Encript.md5(String.format("%s%s", UUID.randomUUID().toString(), System.currentTimeMillis()));
        String sign = Encript.md5(String.format("%s%s%s", appKey, nonce, appSecret));

        String portsUrl = site.getProperty("rhub.ports.wybank.trade");
        String url = String.format("%s" +
                        "?wenyBankID=%s" +
                        "&purchaser=%s" +
                        "&purchaserName=%s" +
                        "&amount=%s" +
                        "&out_trade_sn=%s" +
                        "&note=%s",
                portsUrl,
                purchaseBO.getWenyBankID(),
                purchaseBO.getPurchaser(),
                purchaseBO.getPurchaserName(),
                purchaseBO.getAmount(),
                purchaseBO.getSn(),
                purchaseBO.getNote()
        );

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Rest-Command", "purchase")
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
        PurchasingResult result = new Gson().fromJson(v, PurchasingResult.class);
        return result;
    }

    @CjTransaction
    @Override
    public void settle(PurchasedBO purchasedBO) throws CircuitException {
        settleTradeService.purchase(purchasedBO);
    }

    @CjTransaction
    @Override
    public void sendSettleAck(PurchaseResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/settle/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "purchase");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_settle_purchase.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
