package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IFinanceService;
import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.mapper.PayChannelMapper;
import cj.netos.oc.wallet.mapper.WorkDayMapper;
import cj.netos.oc.wallet.model.*;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;

import java.util.List;
import java.util.Map;

@CjBridge(aspects = "@transaction")
@CjService(name = "financeService")
public class FinanceService implements IFinanceService {
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.WorkDayMapper")
    WorkDayMapper workDayMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.PayChannelMapper")
    PayChannelMapper payChannelMapper;

    @CjServiceRef
    IWalletService walletService;

    @CjTransaction
    @Override
    public String getActivingWorkday(String person) throws CircuitException {
        WorkDay workDay = getTopWorkday();
        if (workDay == null) {
            Map<String, Object> accounts = walletService.getAllAccount(person);
            BalanceAccount balanceAccount = (BalanceAccount) accounts.get("balanceAccount");
            FreezenAccount freezenAccount = (FreezenAccount) accounts.get("freezenAccount");
            ProfitAccount profitAccount = (ProfitAccount) accounts.get("profitAccount");
            AbsorbAccount absorbAccount = (AbsorbAccount) accounts.get("absorbAccount");
            WenyAccount wenyAccount = (WenyAccount) accounts.get("wenyAccount");
            workDay = new WorkDay();
            workDay.setPreAmount(balanceAccount.getAmount());
            workDay.setPreFreezenAmount(freezenAccount.getAmount());
            workDay.setPreProfitAmount(profitAccount.getAmount());
//            workDay.setPreYinjAmount(absorbAccount.getAmount());
//            workDay.setPreWenyQuantities(wenyAccount.getQuanlities());

            workDayMapper.insert(workDay);
            return workDay.getWorkDay();
        }

        switch (workDay.getDayState()) {
            case 0://工作状态,直接返回
                return workDay.getWorkDay();
            case 1:
                //日切中，则插入当前日并返回
            case 2://日切完成，则插入当前日并返回
                workDay = new WorkDay();

                workDayMapper.insert(workDay);
                return workDay.getWorkDay();
        }
        return workDay.getWorkDay();
    }

    @CjTransaction
    @Override
    public boolean isSwitchingWorkDay() {
        WorkDay workDay = getTopWorkday();
        if (workDay == null) {
            return false;
        }
        return workDay.getDayState() == 1;
    }

    @CjTransaction
    @Override
    public WorkDay getTopWorkday() {
        return workDayMapper.getTopWorkday();
    }

    @CjTransaction
    @Override
    public void addPayChannel(PayChannel payChannel) throws CircuitException {
        payChannelMapper.insert(payChannel);
    }

    @CjTransaction
    @Override
    public void removePayChannel(String id) throws CircuitException {
        payChannelMapper.deleteByPrimaryKey(id);
    }

    @CjTransaction
    @Override
    public List<WorkDay> pageWorkday(int limit, int offset) {
        return workDayMapper.pageWorkday(limit, offset);
    }
}
