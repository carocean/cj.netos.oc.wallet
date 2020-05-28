package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IExchangeActivityController;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.bo.ExchangeBO;
import cj.netos.oc.wallet.bo.ExchangedBO;
import cj.netos.oc.wallet.result.ExchangeResult;
import cj.netos.oc.wallet.result.ExchangingResult;
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
@CjService(name = "exchangeActivityController")
public class ExchangeActivityController implements IExchangeActivityController {
    @CjServiceRef(refByName = "@.rabbitmq.producer.ack")
    IRabbitMQProducer rabbitMQProducer;

    @CjServiceSite
    IServiceSite site;

    @CjServiceRef
    ISettleTradeService settleTradeService;
    @CjTransaction
    @Override
    public ExchangeResult doReceipt(ExchangeBO bo) throws CircuitException {
        return call_wenybank_exchange(bo);
    }

    //发纹银银行发起承兑
    private ExchangeResult call_wenybank_exchange(ExchangeBO exchangeBO) throws CircuitException {

        OkHttpClient client = (OkHttpClient) site.getService("@.http");

        String appid = site.getProperty("appid");
        String appKey = site.getProperty("appKey");
        String appSecret = site.getProperty("appSecret");
        String nonce = Encript.md5(String.format("%s%s", UUID.randomUUID().toString(), System.currentTimeMillis()));
        String sign = Encript.md5(String.format("%s%s%s", appKey, nonce, appSecret));

        String portsUrl = site.getProperty("rhub.ports.wybank.trade");
        String url = String.format("%s" +
                        "?purchaseSN=%s" +
                        "&out_trade_sn=%s" +
                        "&note=%s",
                portsUrl,
                exchangeBO.getBankPurchNo(),
                exchangeBO.getSn(),
                exchangeBO.getNote()
        );

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Rest-Command", "exchange")
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
        ExchangeResult result = new Gson().fromJson(v, ExchangeResult.class);
        return result;
    }

    @CjTransaction
    @Override
    public void sendReceiptAck(ExchangingResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "exchange");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        rabbitMQProducer.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }

    @CjTransaction
    @Override
    public ExchangeResult settle(ExchangedBO bo) throws CircuitException {
        return this.settleTradeService.exchange(bo);
    }

    @CjTransaction
    @Override
    public void sendSettleAck(ExchangingResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/settle/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "exchange");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        rabbitMQProducer.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
