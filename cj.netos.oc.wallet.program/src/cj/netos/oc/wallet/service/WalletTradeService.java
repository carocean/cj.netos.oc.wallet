package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IFinanceService;
import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.IWalletTradeService;
import cj.netos.oc.wallet.bo.RechargeBO;
import cj.netos.oc.wallet.mapper.BalanceAccountMapper;
import cj.netos.oc.wallet.mapper.BalanceBillMapper;
import cj.netos.oc.wallet.mapper.RechargeRecordMapper;
import cj.netos.oc.wallet.model.*;
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

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.BalanceBillMapper")
    BalanceBillMapper balanceBillMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.BalanceAccountMapper")
    BalanceAccountMapper balanceAccountMapper;
    @CjServiceRef
    IWalletService walletService;

    @CjServiceRef
    IFinanceService financeService;

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
    public void rechargeDone(String sn, long amount, String code, String message) throws CircuitException {
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectByPrimaryKey(sn);
        if (rechargeRecord == null) {
            throw new CircuitException("404", "没有下单");
        }
        if (rechargeRecord.getState() == 1) {
            throw new CircuitException("404", "订单已完成");
        }
        if (!walletService.isinitWallet(rechargeRecord.getPerson())) {
            throw new CircuitException("500", "没有开通账户:" + rechargeRecord.getPerson());
        }
        updateRechargeRecord(sn, amount, code, message);
        addBalanceBill(rechargeRecord, amount);
    }

    private void updateRechargeRecord(String sn, long amount, String code, String message) {
        String ldtime = WalletUtils.dateTimeToSecond(System.currentTimeMillis());
        rechargeRecordMapper.finish(sn, amount, ldtime, code, message);
    }

    private void addBalanceBill(RechargeRecord rechargeRecord, long amount) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(rechargeRecord.getPerson());
        IdWorker iw1 = new IdWorker(1);
        try {
            balanceBill.setSn(iw1.nextId()+"");
        } catch (Exception e) {
            e.printStackTrace();

        }
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(amount);
        balanceBill.setBalance(balanceAccount.getAmount() + amount);
        balanceBill.setCtime(WalletUtils.dateTimeToSecond(System.currentTimeMillis()));
        balanceBill.setNote(rechargeRecord.getNote());
        balanceBill.setOrder(1);
        balanceBill.setRefsn(rechargeRecord.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle("充值");

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        updateBalanceAccount(balanceAccount, balanceBill.getBalance());
    }

    private void updateBalanceAccount(BalanceAccount balanceAccount, Long balance) {
        balanceAccountMapper.updateAmount(balanceAccount.getId(), balance, WalletUtils.dateTimeToSecond(System.currentTimeMillis()));
    }


}
