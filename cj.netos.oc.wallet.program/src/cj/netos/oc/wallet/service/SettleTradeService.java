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
import java.util.Calendar;

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
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.AbsorbAccountMapper")
    AbsorbAccountMapper absorbAccountMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.AbsorbBillMapper")
    AbsorbBillMapper absorbBillMapper;

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
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(rechargeRecord.getNote());
        balanceBill.setOrder(1);
        balanceBill.setRefsn(rechargeRecord.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle("充值");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        balanceBill.setYear(calendar.get(Calendar.YEAR));
        balanceBill.setMonth(calendar.get(Calendar.MONTH));
        balanceBill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        balanceBill.setSeason(season);

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        updateBalanceAccount(balanceAccount, balanceBill.getBalance());
    }

    private void updateBalanceAccount(BalanceAccount balanceAccount, Long balance) {
        balanceAccountMapper.updateAmount(balanceAccount.getId(), balance, WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

        wenyBillMapper.insert(bill);

        wenyAccountMapper.updateStock(wenyAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));

    }

    @CjTransaction
    @Override
    public ExchangeResult exchange(ExchangedBO bo) {
        if (!walletService.hasWenyBankAccount(bo.getExchanger(), bo.getBankid())) {
            walletService.createWenyBankAccount(bo.getExchanger(), bo.getPersonName(), bo.getBankid());
        }
        addStockBill_sub(bo);
        addProfitBill_add(bo);
        addFreezenBill_sub(bo);
        ExchangeResult result = new ExchangeResult();
        result.load(bo);
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

        wenyBillMapper.insert(bill);

        wenyAccountMapper.updateStock(wenyAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    @CjTransaction
    @Override
    public void depositAbsorb(DepositAbsorbBO bo) {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        addAbsorbBill_add(bo);
    }

    private void addAbsorbBill_add(DepositAbsorbBO bo) {
        AbsorbAccount absorbAccount = walletService.getAbsorbAccount(bo.getPerson());

        AbsorbBill bill = new AbsorbBill();
        bill.setSn(new IdWorker().nextId());
        bill.setAccountid(absorbAccount.getId());
        bill.setAmount(bo.getDemandAmount());
        bill.setBalance(absorbAccount.getAmount() + bo.getDemandAmount());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(1);
        bill.setRefsn(bo.getSn());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        bill.setTitle("存入");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

        absorbBillMapper.insert(bill);

        //驱动余额更新
        updateAbsorbAccount(absorbAccount, bill.getBalance());
    }

    private void updateAbsorbAccount(AbsorbAccount absorbAccount, Long balance) {
        absorbAccountMapper.updateAmount(absorbAccount.getId(), balance, WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    @CjTransaction
    @Override
    public void transAbsorb(TransAbsorbBO bo) throws CircuitException {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        addAbsorbBill_sub(bo);
        addBalanceBill_add_by_absorb(bo);
    }

    private void addBalanceBill_add_by_absorb(TransAbsorbBO bo) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getDemandAmount());
        balanceBill.setBalance(balanceAccount.getAmount() + bo.getDemandAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(10);
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle("转入洇金");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        balanceBill.setYear(calendar.get(Calendar.YEAR));
        balanceBill.setMonth(calendar.get(Calendar.MONTH));
        balanceBill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        balanceBill.setSeason(season);

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        updateBalanceAccount(balanceAccount, balanceBill.getBalance());
    }

    private void addAbsorbBill_sub(TransAbsorbBO bo) throws CircuitException {
        AbsorbAccount absorbAccount = walletService.getAbsorbAccount(bo.getPerson());
        if (absorbAccount.getAmount() < bo.getDemandAmount()) {
            throw new CircuitException("2000", String.format("余额不足"));
        }
        AbsorbBill bill = new AbsorbBill();
        bill.setSn(new IdWorker().nextId());
        bill.setAccountid(absorbAccount.getId());
        bill.setAmount(bo.getDemandAmount() * -1);
        bill.setBalance(absorbAccount.getAmount() - bo.getDemandAmount());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(2);
        bill.setRefsn(bo.getSn());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        bill.setTitle("提取到零钱");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

        absorbBillMapper.insert(bill);

        //驱动余额更新
        updateAbsorbAccount(absorbAccount, bill.getBalance());
    }

    @CjTransaction
    @Override
    public void transProfit(TransProfitBO bo) throws CircuitException {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        if (!walletService.hasWenyBankAccount(bo.getPerson(), bo.getWenyBankID())) {
            walletService.createWenyBankAccount(bo.getPerson(), bo.getPersonName(), bo.getWenyBankID());
        }
        addProfitBill_sub(bo);
        addBalanceBill_add_by_profit(bo);
    }


    private void addBalanceBill_add_by_profit(TransProfitBO bo) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getDemandAmount());
        balanceBill.setBalance(balanceAccount.getAmount() + bo.getDemandAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(11);
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle("转入收益");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        balanceBill.setYear(calendar.get(Calendar.YEAR));
        balanceBill.setMonth(calendar.get(Calendar.MONTH));
        balanceBill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        balanceBill.setSeason(season);

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        updateBalanceAccount(balanceAccount, balanceBill.getBalance());
    }

    private void addProfitBill_sub(TransProfitBO bo) throws CircuitException {
        ProfitAccount profitAccount = walletService.getProfitAccount(bo.getPerson(), bo.getWenyBankID());
        if (profitAccount.getAmount() < bo.getDemandAmount()) {
            throw new CircuitException("2000", String.format("余额不足"));
        }
        ProfitBill bill = new ProfitBill();
        bill.setSn(new IdWorker().nextId());
        bill.setAccountid(profitAccount.getId());
        bill.setAmount(bo.getDemandAmount() * -1);
        bill.setBalance(profitAccount.getAmount() - bo.getDemandAmount());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(2);
        bill.setRefsn(bo.getSn());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        bill.setTitle("提取到零钱");
        bill.setBankid(bo.getWenyBankID());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

        profitBillMapper.insert(bill);

        //驱动余额更新
        updateProfitAccount(profitAccount, bill.getBalance());
    }

    private void updateProfitAccount(ProfitAccount profitAccount, Long balance) {
        profitAccountMapper.updateAmount(profitAccount.getId(), balance, WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }
    @CjTransaction
    @Override
    public void transShunt(WithdrawShunterBO transShuntedBO) {
        if (!walletService.hasWallet(transShuntedBO.getPerson())) {
            walletService.createWallet(transShuntedBO.getPerson(), transShuntedBO.getPersonName());
        }
        if (!walletService.hasWenyBankAccount(transShuntedBO.getPerson(), transShuntedBO.getWenyBankID())) {
            walletService.createWenyBankAccount(transShuntedBO.getPerson(), transShuntedBO.getPersonName(), transShuntedBO.getWenyBankID());
        }
        addBalanceBill_add_by_shunt(transShuntedBO);
    }

    private void addBalanceBill_add_by_shunt(WithdrawShunterBO bo) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getDemandAmount());
        balanceBill.setBalance(balanceAccount.getAmount() + bo.getDemandAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(12);
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle("转入账金");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        balanceBill.setYear(calendar.get(Calendar.YEAR));
        balanceBill.setMonth(calendar.get(Calendar.MONTH));
        balanceBill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        balanceBill.setSeason(season);

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        updateBalanceAccount(balanceAccount, balanceBill.getBalance());
    }
}
