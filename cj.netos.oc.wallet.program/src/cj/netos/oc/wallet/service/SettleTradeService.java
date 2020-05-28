package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IOnorderService;
import cj.netos.oc.wallet.ISettleTradeService;
import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.bo.*;
import cj.netos.oc.wallet.mapper.*;
import cj.netos.oc.wallet.model.*;
import cj.netos.oc.wallet.result.ExchangeResult;
import cj.netos.oc.wallet.util.IdWorker;
import cj.netos.oc.wallet.util.WalletUtils;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;

import java.math.BigDecimal;

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

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.FreezenBillMapper")
    FreezenBillMapper freezenBillMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.FreezenAccountMapper")
    FreezenAccountMapper freezenAccountMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.ProfitAccountMapper")
    ProfitAccountMapper profitAccountMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.ProfitBillMapper")
    ProfitBillMapper profitBillMapper;
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
        if (!walletService.hasWenyBankAccount(purchasedBO.getPerson(), purchasedBO.getBankid())) {
            walletService.createWenyBankAccount(purchasedBO.getPerson(), purchasedBO.getPersonName(), purchasedBO.getBankid());
        }
        addStockBill(purchasedBO);
        addFreezenBill(purchasedBO);
    }

    private void addFreezenBill(PurchasedBO purchasedBO) {
        FreezenAccount freezenAccount = walletService.getFreezenAccount(purchasedBO.getPerson(), purchasedBO.getBankid());
        FreezenBill bill = new FreezenBill();
        bill.setAccountid(freezenAccount.getId());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(purchasedBO.getNote());
        bill.setOrder(8);
        bill.setAmount(purchasedBO.getPrincipalAmount());
        bill.setBalance(freezenAccount.getAmount() + purchasedBO.getPrincipalAmount());
        bill.setRefsn(purchasedBO.getSn());
        bill.setSn(new IdWorker().nextId());
        bill.setTitle("申购");
        bill.setBankid(purchasedBO.getBankid());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        freezenBillMapper.insert(bill);

        freezenAccountMapper.updateAmount(freezenAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    private void addStockBill(PurchasedBO purchasedBO) {
        WenyAccount wenyAccount = walletService.getWenyAccount(purchasedBO.getPerson(), purchasedBO.getBankid());
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
        bill.setBankid(purchasedBO.getBankid());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        wenyBillMapper.insert(bill);

        wenyAccountMapper.updateStock(wenyAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));

    }

    @CjTransaction
    @Override
    public ExchangeResult exchange(ExchangedBO bo) {
        if (!walletService.hasWenyBankAccount(bo.getExchanger(), bo.getBankid())) {
            walletService.createWenyBankAccount(bo.getExchanger(), bo.getPersonName(), bo.getBankid());
        }
        ExchangeResult result = new ExchangeResult();
        addStockBill_sub(bo);
        addProfitBill_add(bo);
        addFreezenBill_sub(bo);
        return result;
    }

    private void addFreezenBill_sub(ExchangedBO bo) {
        FreezenAccount freezenAccount = walletService.getFreezenAccount(bo.getExchanger(), bo.getBankid());
        FreezenBill bill = new FreezenBill();
        bill.setAccountid(freezenAccount.getId());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(9);
        bill.setAmount(bo.getPrincipalAmount() * -1);
        bill.setBalance(freezenAccount.getAmount() - bo.getPrincipalAmount());
        bill.setRefsn(bo.getSn());
        bill.setSn(new IdWorker().nextId());
        bill.setTitle("承兑");
        bill.setBankid(bo.getBankid());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        freezenBillMapper.insert(bill);

        freezenAccountMapper.updateAmount(freezenAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    private void addProfitBill_add(ExchangedBO bo) {
        ProfitAccount profitAccount = walletService.getProfitAccount(bo.getExchanger(), bo.getBankid());
        ProfitBill bill = new ProfitBill();
        bill.setAccountid(profitAccount.getId());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(9);
        bill.setAmount(bo.getProfit());
        bill.setBalance(profitAccount.getAmount() + bo.getProfit());
        bill.setRefsn(bo.getSn());
        bill.setSn(new IdWorker().nextId());
        bill.setTitle("承兑");
        bill.setBankid(bo.getBankid());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        profitBillMapper.insert(bill);

        profitAccountMapper.updateAmount(profitAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));

    }

    private void addStockBill_sub(ExchangedBO bo) {
        WenyAccount wenyAccount = walletService.getWenyAccount(bo.getExchanger(), bo.getBankid());
        WenyBill bill = new WenyBill();
        bill.setBankid(bo.getBankid());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        bill.setTitle("承兑");
        bill.setSn(new IdWorker().nextId());
        bill.setRefsn(bo.getSn());
        bill.setOrder(9);
        bill.setNote(bo.getNote());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setAccountid(wenyAccount.getId());
        bill.setStock(bo.getStock().multiply(new BigDecimal(-1.0)));
        bill.setBalance(wenyAccount.getStock().subtract(bo.getStock()));
        wenyBillMapper.insert(bill);

        wenyAccountMapper.updateStock(wenyAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }
}
