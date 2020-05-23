package cj.netos.oc.wallet.cmd;

import cj.netos.oc.wallet.bo.PurchaseBO;
import cj.netos.rabbitmq.CjConsumer;
import cj.netos.rabbitmq.RabbitMQException;
import cj.netos.rabbitmq.RetryCommandException;
import cj.netos.rabbitmq.consumer.IConsumerCommand;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.annotation.CjServiceSite;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.util.Encript;
import cj.ultimate.gson2.com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CjConsumer(name = "trade")
@CjService(name = "/trade/weny.mq#purchase")
public class PurchaseWenyCommand implements IConsumerCommand {
    @CjServiceSite
    IServiceSite site;


    @Override
    public void command(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws RabbitMQException, RetryCommandException, IOException {
        PurchaseBO purchaseBO = new Gson().fromJson(new String(body), PurchaseBO.class);
        try {
            Map<String, Object> result = call_wenybank_purchase(purchaseBO);
            System.out.println("-----" + result);
        } catch (CircuitException e) {
            e.printStackTrace();
        }
    }

    //发纹银银行发起承兑
    private Map<String, Object> call_wenybank_purchase(PurchaseBO purchaseBO) throws CircuitException {

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
        Map<String, Object> result = new Gson().fromJson(v, HashMap.class);
        return result;
    }
}
