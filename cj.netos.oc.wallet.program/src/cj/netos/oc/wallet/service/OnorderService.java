package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IOnorderService;
import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.bo.OnorderBO;
import cj.netos.oc.wallet.bo.WithdrawBO;
import cj.netos.oc.wallet.mapper.BalanceAccountMapper;
import cj.netos.oc.wallet.mapper.BalanceBillMapper;
import cj.netos.oc.wallet.mapper.OnorderBillMapper;
import cj.netos.oc.wallet.mapper.RootAccountMapper;
import cj.netos.oc.wallet.model.*;
import cj.netos.oc.wallet.util.IdWorker;
import cj.netos.oc.wallet.util.WalletUtils;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;

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

    @CjServiceRef
    IWalletService walletService;

    @CjTransaction
    @Override
    public void put(OnorderBO bo) throws CircuitException {
        if (!walletService.hasWallet(bo.getPerson())) {
            walletService.createWallet(bo.getPerson(), bo.getPersonName());
        }
        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        if (balanceAccount.getAmount() < bo.getAmount()) {
            throw new CircuitException("2000", String.format("余额不足，提现失败。订单:%s", bo.getRefsn()));
        }
        addBalanceBillByOnorder(bo, bo.getAmount());
        addOnorderBill(bo, balanceAccount);
    }

    private void addBalanceBillByOnorder(OnorderBO bo, long amount) throws CircuitException {
        BalanceBill balanceBill = new BalanceBill();

        BalanceAccount balanceAccount = walletService.getBalanceAccount(bo.getPerson());
        balanceBill.setSn(new IdWorker().nextId());
        balanceBill.setAccountid(balanceAccount.getId());
        balanceBill.setAmount(amount * -1);
        balanceBill.setBalance(balanceAccount.getAmount() - amount);
        balanceBill.setCtime(WalletUtils.dateTimeToSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(bo.getOrder());
        balanceBill.setRefsn(bo.getRefsn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle(bo.getCause());

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        balanceAccountMapper.updateAmount(balanceAccount.getId(), balanceBill.getBalance(), WalletUtils.dateTimeToSecond(System.currentTimeMillis()));
    }

    private void addOnorderBill(OnorderBO bo, BalanceAccount balanceAccount) {
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
        balanceBill.setCtime(WalletUtils.dateTimeToSecond(System.currentTimeMillis()));
        balanceBill.setNote(bo.getNote());
        balanceBill.setOrder(bo.getOrder());
        balanceBill.setRefsn(bo.getRefsn());
//        String workSwitchDay = financeService.getActivingWorkday(rechargeRecord.getPerson());
        balanceBill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));
        balanceBill.setTitle(bo.getCause());

        balanceBillMapper.insert(balanceBill);

        //驱动余额更新
        balanceAccountMapper.updateAmount(balanceAccount.getId(), balanceBill.getBalance(), WalletUtils.dateTimeToSecond(System.currentTimeMillis()));
    }

    private void addOnorderBillForSub(OnorderBO bo) throws CircuitException {
        RootAccount rootAccount = walletService.getRootAccount(bo.getPerson());
        if (rootAccount.getOnorderAmount() < bo.getAmount()) {
            throw new CircuitException("2000", "在订单资金余额不足扣除");
        }
        OnorderBill bill = new OnorderBill();
        bill.setAccountid(rootAccount.getId());
        bill.setAmount(bo.getAmount()*-1);
        bill.setBalance(rootAccount.getOnorderAmount() - bo.getAmount());
        bill.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        bill.setNote(bo.getNote());
        bill.setOrder(bo.getOrder());
        bill.setRefsn(bo.getRefsn());
        bill.setSn(new IdWorker().nextId());
        bill.setTitle(bo.getCause());
        bill.setWorkday(WalletUtils.dateTimeToDay(System.currentTimeMillis()));

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
        bo.setCause(bo.getCause()+"-归还到余额");
        addOnorderBillForSub(bo);
        bo.setAmount(returnAmount);
        addBalanceBillForAddByOnorder(bo);
    }

}
