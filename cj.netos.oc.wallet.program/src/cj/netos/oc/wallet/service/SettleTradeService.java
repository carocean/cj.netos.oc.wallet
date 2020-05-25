package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IOnorderService;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.bo.*;
import cj.netos.oc.wallet.mapper.BalanceAccountMapper;
import cj.netos.oc.wallet.mapper.BalanceBillMapper;
import cj.netos.oc.wallet.mapper.WenyAccountMapper;
import cj.netos.oc.wallet.mapper.WenyBillMapper;
import cj.netos.oc.wallet.model.BalanceAccount;
import cj.netos.oc.wallet.model.BalanceBill;
import cj.netos.oc.wallet.model.WenyAccount;
import cj.netos.oc.wallet.model.WenyBill;
import cj.netos.oc.wallet.result.PurchasedResult;
import cj.netos.oc.wallet.util.IdWorker;
import cj.netos.oc.wallet.util.WalletUtils;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;

@CjBridge(aspects = "@transaction")
@CjService(name = "settleTradeService")
public class SettleTradeService implements ISettleTradeService {


    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.BalanceBillMapper")
    BalanceBillMapper balanceBillMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.BalanceAccountMapper")
    BalanceAccountMapper balanceAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.WenyBillMapper")
    WenyBillMapper wenyBillMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.WenyAccountMapper")
    WenyAccountMapper wenyAccountMapper;

    @CjServiceRef
    IWalletService walletService;
    @CjServiceRef
    IOnorderService onorderService;

    @CjTransaction
    @Override
    public void recharge(RechargeBO bo) throws CircuitException {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        addBalanceBillByRecharge(bo, bo.getRealAmount());
    }


    private void addBalanceBillByRecharge(RechargeBO rechargeRecord, long amount) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(rechargeRecord.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
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

    @CjTransaction
    @Override
    public void withdraw(WithdrawBO withdrawBO) throws CircuitException {
        OnorderBO bo = new OnorderBO();
        bo.setAmount(withdrawBO.getRealAmount());
        bo.setCause("决清");
        bo.setNote(withdrawBO.getNote());
        bo.setOrder(2);
        bo.setPerson(withdrawBO.getPerson());
        bo.setPersonName(withdrawBO.getPersonName());
        bo.setRefsn(withdrawBO.getSn());
        bo.setRefType("withdraw");
        onorderService.done(bo);
    }
    @CjTransaction
    @Override
    public void purchase(PurchasedBO purchasedBO) throws CircuitException {
        OnorderBO bo = new OnorderBO();
        bo.setAmount(purchasedBO.getPurchAmount());
        bo.setCause("决清");
        bo.setNote(purchasedBO.getNote());
        bo.setOrder(8);
        bo.setPerson(purchasedBO.getPerson());
        bo.setPersonName(purchasedBO.getPersonName());
        bo.setRefsn(purchasedBO.getSn());
        bo.setRefType("purchase");
        onorderService.done(bo);

        if (!walletService.hasWallet(purchasedBO.getPerson())) {
            walletService.createWallet(purchasedBO.getPerson(), purchasedBO.getPersonName());
        }
        addStockBill(purchasedBO);
    }

    private void addStockBill(PurchasedBO purchasedBO) {
        WenyAccount wenyAccount = walletService.getWenyAccount(purchasedBO.getPerson());
        WenyBill bill = new WenyBill();
        bill.setAccountid(wenyAccount.getId());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(purchasedBO.getNote());
        bill.setOrder(8);
        bill.setStock(purchasedBO.getStock());
        bill.setBalance(wenyAccount.getStock().add(purchasedBO.getStock()));
        bill.setRefsn(purchasedBO.getSn());
        bill.setSn(new IdWorker().nextId());
        bill.setTitle("申购");
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        wenyBillMapper.insert(bill);

        wenyAccountMapper.updateStock(wenyAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));

    }
}
