package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IOnorderService;
import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.bo.OnorderBO;
import cj.netos.oc.wallet.bo.WithdrawBO;
import cj.netos.oc.wallet.mapper.*;
import cj.netos.oc.wallet.model.*;
import cj.netos.oc.wallet.util.IdWorker;
import cj.netos.oc.wallet.util.WalletUtils;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;

import java.util.Calendar;
import java.util.List;

@CjBridge(aspects = "@transaction")
@CjService(name = "onorderService")
public class OnorderService implements IOnorderService {
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.BalanceBillMapper")
    BalanceBillMapper balanceBillMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.BalanceAccountMapper")
    BalanceAccountMapper balanceAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.OnorderBillMapper")
    OnorderBillMapper onorderBillMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.RootAccountMapper")
    RootAccountMapper rootAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.TrialAccountMapper")
    TrialAccountMapper trialAccountMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.TrialBillMapper")
    TrialBillMapper trialBillMapper;

    @CjServiceRef
    IWalletService walletService;

    @CjTransaction
    @Override
    public void put2(OnorderBO bo) throws CircuitException {//从体验金账户扣款
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
        //从体验金账户中扣款，之后若决清失败充许体验金还回到零钱账户中，因为都是客户的钱嘛，而且确实体验了发文流程，只是出错了嘛，出错了就该给客户随便用
        if (trialAccount.getAmount() < bo.getAmount()) {
            throw new CircuitException("2000", String.format("体验金不足。订单:%s", bo.getRefsn()));
        }
        addTrialBillByOnorder(bo, bo.getAmount());
        addOnorderBill(bo);
    }

    private void addTrialBillByOnorder(OnorderBO bo, long amount) {
        TrialBill trialBill = new TrialBill();

        TrialAccount trialAccount = walletService.getTrialAccount(bo.getPerson());
        trialBill.setSn(new IdWorker().nextId());
        trialBill.setAccountid(trialAccount.getId());
        trialBill.setAmount(amount * -1);
        trialBill.setBalance(trialAccount.getAmount() - amount);
        trialBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        trialBill.setNote(bo.getNote());
        trialBill.setOrder(2);
        trialBill.setRefsn(bo.getRefsn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        trialBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        trialBill.setTitle(bo.getCause());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        trialBill.setYear(calendar.get(Calendar.YEAR));
        trialBill.setMonth(calendar.get(Calendar.MONTH));
        trialBill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        trialBill.setSeason(season);

        trialBillMapper.insert(trialBill);

        //驱动余额更新
        trialAccountMapper.updateAmount(trialAccount.getId(), trialBill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    @CjTransaction
    @Override
    public void put(OnorderBO bo) throws CircuitException {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        if (balanceAccount.getAmount() < bo.getAmount()) {
            throw new CircuitException("2000", String.format("余额不足。订单:%s", bo.getRefsn()));
        }
        addBalanceBillByOnorder(bo, bo.getAmount());
        addOnorderBill(bo);
    }

    private void addBalanceBillByOnorder(OnorderBO bo, long amount) throws CircuitException {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(amount * -1);
        balanceBill.setBalance(balanceAccount.getAmount() - amount);
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(bo.getOrder());
        balanceBill.setRefsn(bo.getRefsn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle(bo.getCause());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        balanceBill.setYear(calendar.get(Calendar.YEAR));
        balanceBill.setMonth(calendar.get(Calendar.MONTH));
        balanceBill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        balanceBill.setSeason(season);

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        balanceAccountMapper.updateAmount(balanceAccount.getId(), balanceBill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    private void addOnorderBill(OnorderBO bo) {
        RootAccount rootAccount = walletService.getRootAccount(bo.getPerson());

        OnorderBill bill = new OnorderBill();
        bill.setAccountid(rootAccount.getId());
        bill.setAmount(bo.getAmount());
        bill.setBalance(rootAccount.getOnorderAmount() + bo.getAmount());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(bo.getOrder());
        bill.setRefsn(bo.getRefsn());
        bill.setSn(new IdWorker().nextId());
        bill.setTitle(bo.getCause());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

        onorderBillMapper.insert(bill);

        rootAccountMapper.setAmountOnorder(rootAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    //如果订单决清失败则还回
    @CjTransaction
    @Override
    public void returnOrder(OnorderBO bo) throws CircuitException {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }

        addOnorderBillForSub(bo);
        addBalanceBillForAddByOnorder(bo);
    }

    private void addBalanceBillForAddByOnorder(OnorderBO bo) {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(bo.getAmount());
        balanceBill.setBalance(balanceAccount.getAmount() + bo.getAmount());
        balanceBill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(bo.getOrder());
        balanceBill.setRefsn(bo.getRefsn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle(bo.getCause());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        balanceBill.setYear(calendar.get(Calendar.YEAR));
        balanceBill.setMonth(calendar.get(Calendar.MONTH));
        balanceBill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        balanceBill.setSeason(season);

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        balanceAccountMapper.updateAmount(balanceAccount.getId(), balanceBill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    private void addOnorderBillForSub(OnorderBO bo) throws CircuitException {
        RootAccount rootAccount = walletService.getRootAccount(bo.getPerson());
        if (rootAccount.getOnorderAmount() < bo.getAmount()) {
            throw new CircuitException("2000", "在订单资金余额不足扣除");
        }
        OnorderBill bill = new OnorderBill();
        bill.setAccountid(rootAccount.getId());
        bill.setAmount(bo.getAmount() * -1);
        bill.setBalance(rootAccount.getOnorderAmount() - bo.getAmount());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(bo.getOrder());
        bill.setRefsn(bo.getRefsn());
        bill.setSn(new IdWorker().nextId());
        bill.setTitle(bo.getCause());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bill.setYear(calendar.get(Calendar.YEAR));
        bill.setMonth(calendar.get(Calendar.MONTH));
        bill.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int season = calendar.get(Calendar.MONTH) % 4;
        bill.setSeason(season);

        onorderBillMapper.insert(bill);

        rootAccountMapper.setAmountOnorder(rootAccount.getId(), bill.getBalance(), WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
    }

    //完成订单则从冻结金中扣除，但不还回到余额账户
    @CjTransaction
    @Override
    public void done(OnorderBO bo) throws CircuitException {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        //计算还回多少，如果小于上一笔预申购的钱，应该将余下的钱还回其余额账户
        OnorderBillExample example = new OnorderBillExample();
        example.createCriteria().andRefsnEqualTo(bo.getRefsn()).andOrderEqualTo(bo.getOrder());
        List<OnorderBill> bills = onorderBillMapper.selectByExample(example);
        if (bills.isEmpty()) {
            throw new CircuitException("404", String.format("先前的预订单不存在。订单号:%s， 类型：%s", bo.getRefsn(), bo.getRefType()));
        }
        OnorderBill bill = bills.get(0);
        long returnAmount = bill.getAmount() - bo.getAmount();
        addOnorderBillForSub(bo);
        if (returnAmount == 0) {
            return;
        }
        bo.setAmount(returnAmount);
        bo.setCause(bo.getCause() + "-归还到余额");
        addOnorderBillForSub(bo);//再把要归还的从在订单扣减
        bo.setAmount(returnAmount);
        addBalanceBillForAddByOnorder(bo);
    }

}
