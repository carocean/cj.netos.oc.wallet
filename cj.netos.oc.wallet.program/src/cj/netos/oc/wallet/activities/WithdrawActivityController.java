package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IOnorderService;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.IWithdrawActivityController;
import cj.netos.oc.wallet.bo.OnorderBO;
import cj.netos.oc.wallet.bo.WithdrawBO;
import cj.netos.oc.wallet.result.WithdrawResult;
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
@CjService(name = "withdrawActivityController")
public class WithdrawActivityController implements IWithdrawActivityController {

    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_receipt_withdraw")
    IRabbitMQProducer toGateway_ack_receipt_withdraw;

    @CjServiceRef(refByName = "@.rabbitmq.producer.toGateway_ack_settle_withdraw")
    IRabbitMQProducer toGateway_ack_settle_withdraw;

    @CjServiceRef
    ISettleTradeService settleTradeService;

    @CjServiceRef
    IOnorderService onorderService;

    @CjTransaction
    @Override
    public void doReceipt(WithdrawBO withdrawBO) throws CircuitException {

        OnorderBO bo = new OnorderBO();
        bo.setCause("提现");
        bo.setNote(withdrawBO.getNote());
        bo.setAmount(withdrawBO.getDemandAmount());
        bo.setRefType("withdraw");
        bo.setRefsn(withdrawBO.getSn());
        bo.setPersonName(withdrawBO.getPersonName());
        bo.setPerson(withdrawBO.getPerson());
        bo.setOrder(2);
        onorderService.put(bo);

    }

    @CjTransaction
    @Override
    public void sendReceiptAck(WithdrawResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "withdraw");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_withdraw.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }

    @CjTransaction
    @Override
    public void cancelReceipt(WithdrawBO withdrawBO) throws CircuitException {

        OnorderBO bo = new OnorderBO();
        bo.setCause("提现撤销");
        bo.setNote(withdrawBO.getNote());
        bo.setAmount(withdrawBO.getDemandAmount());
        bo.setRefType("withdraw");
        bo.setRefsn(withdrawBO.getSn());
        bo.setPersonName(withdrawBO.getPersonName());
        bo.setPerson(withdrawBO.getPerson());
        bo.setOrder(7);
        onorderService.returnOrder(bo);

    }

    @CjTransaction
    @Override
    public void sendCancelReceiptAck(WithdrawResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/receipt/ackCancelReceipt.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "withdraw");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_receipt_withdraw.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }

    @CjTransaction
    @Override
    public void settle(WithdrawBO withdrawBO) throws CircuitException {
        settleTradeService.withdraw(withdrawBO);
    }

    @CjTransaction
    @Override
    public void sendSettleAck(WithdrawResult result) throws CircuitException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .type("/trade/settle/ack.mhub")
                .headers(new HashMap<String, Object>() {{
                    put("command", "withdraw");
                    put("person", result.getPerson());
                    put("record_sn", result.getSn());
                }})
                .build();
        toGateway_ack_settle_withdraw.publish("gateway", properties, new Gson().toJson(result).getBytes());
    }
}
