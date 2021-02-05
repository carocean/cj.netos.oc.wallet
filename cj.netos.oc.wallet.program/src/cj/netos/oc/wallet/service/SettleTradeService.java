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
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.TrialAccountMapper")
    TrialAccountMapper trialAccountMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.TrialBillMapper")
    TrialBillMapper trialBillMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.FeeAccountMapper")
    FeeAccountMapper feeAccountMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.FeeBillMapper")
    FeeBillMapper feeBillMapper;
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
        bo.setAmount(withdrawBO.getDemandAmount());
        bo.setCause("决清");
        bo.setNote(withdrawBO.getNote());
        bo.setOrder(2);
        bo.setPerson(withdrawBO.getPerson());
        bo.setPersonName(withdrawBO.getPersonName());
        bo.setRefsn(withdrawBO.getSn());
        bo.setRefType("withdraw");
        onorderService.done(bo);
        //记账服务费
        addBalanceFee(withdrawBO);
    }

    private void addBalanceFee(WithdrawBO bo) {
        if (!walletService.hasFeeAccount(bo.getPayAccount())) {
            walletService.createFeeAccount(bo.getPayChannel(), bo.getPayAccount());
        }
        FeeAccount feeAccount = walletService.getFeeAccount(bo.getPayAccount());
        FeeBill bill = new FeeBill();

        bill.setSn(new IdWorker().nextId());
        bill.setAccountid(feeAccount.getId());
        bill.setAmount(bo.getFeeAmount());
        bill.setBalance(feeAccount.getAmount() + bill.getAmount());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(0);
        bill.setRefsn(bo.getSn());
        bill.setPayChannel(bo.getPayChannel());
        bill.setChannelAccount(bo.getPayAccount());
        bill.setPersonCard(bo.getPersonCard());

//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        bill.setTitle("提现手续费");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

        feeBillMapper.insert(bill);

        //驱动余额更新
        feeAccountMapper.updateAmount(feeAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
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
        addBalanceBill_add(bo);
        ExchangeResult result = new ExchangeResult();
        result.load(bo);
        return result;
    }

    private void addBalanceBill_add(ExchangedBO bo) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getExchanger());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getAmount());//承兑是直接将所得打到余额账户，至于收益金账户仅用于统计赢亏，因此收益金账户不用于提现
        balanceBill.setBalance(balanceAccount.getAmount() + balanceBill.getAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote() + "");
        balanceBill.setOrder(9);
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle("承兑纹银最终获得");

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

    private void addFreezenBill_sub(ExchangedBO bo) {
        FreezenAccount freezenAccount = walletService.getFreezenAccount(bo.getExchanger(), bo.getBankid());
        FreezenBill bill = new FreezenBill();
        bill.setAccountid(freezenAccount.getId());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(9);
        bill.setAmount(bo.getPrincipalAmount() * -1);
        bill.setBalance(freezenAccount.getAmount() + bill.getAmount());
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
        bill.setBalance(profitAccount.getAmount() + bill.getAmount());
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
        bill.setBalance(wenyAccount.getStock().add(bill.getStock()));

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
    public void moduleTransin(ModuleTransinBO bo) {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        addModuleTransinBill_add(bo);
    }

    private void addModuleTransinBill_add(ModuleTransinBO bo) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getAmount());//承兑是直接将所得打到余额账户，至于收益金账户仅用于统计赢亏，因此收益金账户不用于提现
        balanceBill.setBalance(balanceAccount.getAmount() + balanceBill.getAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(14);
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle(String.format("【%s】服务转入账款",bo.getModuleTitle()));

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
        String note = bo.getNote();
        AbsorbBill bill = new AbsorbBill();
        bill.setSn(new IdWorker().nextId());
        bill.setAccountid(absorbAccount.getId());
        bill.setAmount(bo.getDemandAmount());
        bill.setBalance(absorbAccount.getAmount().add(bo.getDemandAmount()));
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(note);
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

    private void updateAbsorbAccount(AbsorbAccount absorbAccount, BigDecimal balance) {
        absorbAccountMapper.updateAmount(absorbAccount.getId(), balance, WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    @CjTransaction
    @Override
    public void depositTrialFunds(DepositTrialBO bo) {
        //如果已创建了钱包账号，则要检查是否已创建了体验金账号
        TrialAccount trialAccount =null;
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
            trialAccount = walletService.getTrialAccount(bo.getPerson());
        } else {
             trialAccount = walletService.getTrialAccount(bo.getPerson());
            if (trialAccount == null) {
                walletService.createTrialAccount(bo.getPerson(),bo.getPersonName());
                trialAccount = walletService.getTrialAccount(bo.getPerson());
            }
        }
        addTrialFundsBill_add(trialAccount,bo);
    }

    private void addTrialFundsBill_add(TrialAccount trialAccount, DepositTrialBO bo) {

        String note = bo.getNote();
        TrialBill bill = new TrialBill();
        bill.setSn(new IdWorker().nextId());
        bill.setAccountid(trialAccount.getId());
        bill.setAmount(bo.getAmount());
        bill.setBalance(trialAccount.getAmount() + bo.getAmount());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(note);
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

        trialBillMapper.insert(bill);

        //驱动余额更新
        updateTrialAccount(trialAccount, bill.getBalance());
    }

    private void updateTrialAccount(TrialAccount trialAccount, Long balance) {
        trialAccountMapper.updateAmount(trialAccount.getId(), balance, WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    @CjTransaction
    @Override
    public void depositHubTails(DepositHubTailsBO bo) {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        addBalanceBill_add_by_hub_tails(bo);
    }

    private void addBalanceBill_add_by_hub_tails(DepositHubTailsBO bo) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getAmount());
        balanceBill.setBalance(balanceAccount.getAmount() + bo.getAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(13);
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle("转入经营尾金");

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
        BigDecimal amount = new BigDecimal(bo.getDemandAmount() + "");
        if (absorbAccount.getAmount().compareTo(amount) < 0) {
            throw new CircuitException("2000", String.format("余额不足"));
        }
        AbsorbBill bill = new AbsorbBill();
        bill.setSn(new IdWorker().nextId());
        bill.setAccountid(absorbAccount.getId());
        bill.setAmount(amount.multiply(new BigDecimal("-1")));
        bill.setBalance(absorbAccount.getAmount().subtract(amount));
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
//
//    @CjTransaction
//    @Override
//    public void transProfit(TransProfitBO bo) throws CircuitException {
//        if (!walletService.hasWallet(bo.getPerson())) {
//            walletService.createWallet(bo.getPerson(), bo.getPersonName());
//        }
//        if (!walletService.hasWenyBankAccount(bo.getPerson(), bo.getWenyBankID())) {
//            walletService.createWenyBankAccount(bo.getPerson(), bo.getPersonName(), bo.getWenyBankID());
//        }
//        addProfitBill_sub(bo);
//        addBalanceBill_add_by_profit(bo);
//    }

//
//    private void addBalanceBill_add_by_profit(TransProfitBO bo) {
//        BalanceBill balanceBill = new BalanceBill();
//
//        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
//        balanceBill.setSn(new IdWorker().nextId());
//        balanceBill.setAccountid(balanceAccount.getId());
//        balanceBill.setAmount(bo.getDemandAmount());
//        balanceBill.setBalance(balanceAccount.getAmount() + bo.getDemandAmount());
//        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
//        balanceBill.setNote(bo.getNote());
//        balanceBill.setOrder(11);
//        balanceBill.setRefsn(bo.getSn());
////        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
//        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
//        balanceBill.setTitle("转入收益");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        balanceBill.setYear(calendar.get(Calendar.YEAR));
//        balanceBill.setMonth(calendar.get(Calendar.MONTH));
//        balanceBill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
//        int season = calendar.get(Calendar.MONTH) % 4;
//        balanceBill.setSeason(season);
//
//        balanceBillMapper.insert(balanceBill);
//
//        //驱动余额更新
//        updateBalanceAccount(balanceAccount, balanceBill.getBalance());
//    }
//
//    private void addProfitBill_sub(TransProfitBO bo) throws CircuitException {
//        ProfitAccount profitAccount = walletService.getProfitAccount(bo.getPerson(), bo.getWenyBankID());
//        if (profitAccount.getAmount() < bo.getDemandAmount()) {
//            throw new CircuitException("2000", String.format("余额不足"));
//        }
//        ProfitBill bill = new ProfitBill();
//        bill.setSn(new IdWorker().nextId());
//        bill.setAccountid(profitAccount.getId());
//        bill.setAmount(bo.getDemandAmount() * -1);
//        bill.setBalance(profitAccount.getAmount() + bill.getAmount());
//        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
//        bill.setNote(bo.getNote());
//        bill.setOrder(2);
//        bill.setRefsn(bo.getSn());
//        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
//        bill.setTitle("提取到零钱");
//        bill.setBankid(bo.getWenyBankID());
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        bill.setYear(calendar.get(Calendar.YEAR));
//        bill.setMonth(calendar.get(Calendar.MONTH));
//        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
//        int season = calendar.get(Calendar.MONTH) % 4;
//        bill.setSeason(season);
//
//        profitBillMapper.insert(bill);
//
//        //驱动余额更新
//        updateProfitAccount(profitAccount, bill.getBalance());
//    }
//
//    private void updateProfitAccount(ProfitAccount profitAccount, Long balance) {
//        profitAccountMapper.updateAmount(profitAccount.getId(), balance, WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
//    }

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

    @CjTransaction
    @Override
    public void payTrade(PayBO payBO) throws CircuitException {
        if (!walletService.hasWallet(payBO.getPerson())) {
            walletService.createWallet(payBO.getPerson(), payBO.getPersonName());
        }
        addBalanceBill_add_by_payment(payBO);
    }

    private void addBalanceBill_add_by_payment(PayBO bo) throws CircuitException {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        if (balanceAccount.getAmount() < bo.getAmount()) {
            throw new CircuitException("500", String.format("余额不足"));
        }
        PayDetailsBO detailsBO = bo.getDetails();
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getAmount() * -1);
        balanceBill.setBalance(balanceAccount.getAmount() - bo.getAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(4);
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle(detailsBO.getOrderTitle());

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

    @CjTransaction
    @Override
    public void p2p(P2PBO pbo) throws CircuitException {
        if (!walletService.hasWallet(pbo.getPayer())) {
            walletService.createWallet(pbo.getPayer(), pbo.getPayerName());
        }
        if (!walletService.hasWallet(pbo.getPayee())) {
            walletService.createWallet(pbo.getPayee(), pbo.getPayeeName());
        }
        addBalanceBill_add_by_payer_p2p(pbo);
        addBalanceBill_add_by_payee_p2p(pbo);
    }

    private void addBalanceBill_add_by_payee_p2p(P2PBO bo) throws CircuitException {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPayee());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getAmount());
        balanceBill.setBalance(balanceAccount.getAmount() + bo.getAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        String title = "";
//        switch (bo.getDirect()) {
//            case "to":
//                title = String.format("转入自:%s", bo.getPayerName());
//                break;
//            case "from":
//                title = String.format("收款自:%s", bo.getPayerName());
//                break;
//        }
        title = String.format("收款自:%s", bo.getPayerName());
        balanceBill.setTitle(title);
        balanceBill.setOrder(3);//付方账单一律视为转账，虽然名为支付

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

    private void addBalanceBill_add_by_payer_p2p(P2PBO bo) throws CircuitException {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPayer());
        if (balanceAccount.getAmount() < bo.getAmount()) {
            throw new CircuitException("500", String.format("余额不足"));
        }
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getAmount() * -1);
        balanceBill.setBalance(balanceAccount.getAmount() - bo.getAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setRefsn(bo.getSn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        String title = "";
//        switch (bo.getDirect()) {
//            case "to":
//                title = String.format("转账给:%s", bo.getPayeeName());
//                break;
//            case "from":
//                title = String.format("付款给:%s", bo.getPayeeName());
//                break;
//        }
        title = String.format("付款给:%s", bo.getPayeeName());
        balanceBill.setTitle(title);
        balanceBill.setOrder(3);//付方账单一律视为转账，虽然名为支付

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
