package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IWalletTradeService;
import cj.netos.oc.wallet.bo.RechargeBO;
import cj.netos.oc.wallet.mapper.RechargeRecordMapper;
import cj.netos.oc.wallet.model.BalanceBill;
import cj.netos.oc.wallet.model.RechargeRecord;
import cj.netos.oc.wallet.model.RechargeRecordExample;
import cj.netos.oc.wallet.util.IdWorker;
import cj.netos.oc.wallet.util.WalletUtils;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;

@CjBridge(aspects = "@transaction")
@CjService(name = "walletTradeService")
public class WalletTradeService implements IWalletTradeService {
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.RechargeRecordMapper")
    RechargeRecordMapper rechargeRecordMapper;

    @CjTransaction
    @Override
    public RechargeRecord addRechargeOrder(RechargeBO recharge) throws CircuitException {
        return addRechargeRecorder(recharge);
    }


    private RechargeRecord addRechargeRecorder(RechargeBO recharge) throws CircuitException {
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setDemandAmount(recharge.getAmount());
        rechargeRecord.setCurrency(recharge.getCurrency());
        rechargeRecord.setFromChannel(recharge.getPaymentChannelID());
        rechargeRecord.setPerson(recharge.getRecharger());
        rechargeRecord.setState(0);
        rechargeRecord.setCtime(WalletUtils.dateTimeToSecond(recharge.getCtime()));
        rechargeRecord.setLutime(WalletUtils.dateTimeToSecond(System.currentTimeMillis()));
        rechargeRecord.setPersonName(recharge.getRechargerName());
        rechargeRecord.setNote(recharge.getNote());
        IdWorker iw1 = new IdWorker(1);
        try {
            rechargeRecord.setSn(iw1.nextId() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        rechargeRecordMapper.insert(rechargeRecord);
        return rechargeRecord;
    }

    @CjTransaction
    @Override
    public void rechargeDone(String sn, long amount,String message) throws CircuitException {
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectByPrimaryKey(sn);
        if (rechargeRecord == null) {
            throw new CircuitException("404", "没有下单");
        }
        updateRechargeRecord(sn, amount,message);
        BalanceBill balanceBill = addBalanceBill(rechargeRecord);
        updateBalanceAccount(balanceBill);
    }

    private void updateRechargeRecord(String sn, long amount, String message) {
        String ldtime = WalletUtils.dateTimeToSecond(System.currentTimeMillis());
        rechargeRecordMapper.finish(sn, amount, ldtime, message);
    }

    private void updateBalanceAccount(BalanceBill rechargeRecord) {

    }

    private BalanceBill addBalanceBill(RechargeRecord rechargeRecord) {
        BalanceBill balanceBill = null;

        return balanceBill;
    }
}
