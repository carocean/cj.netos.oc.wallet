package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IBillService;
import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.mapper.*;
import cj.netos.oc.wallet.model.*;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.orm.mybatis.annotation.CjTransaction;

import java.util.List;

@CjBridge(aspects = "@transaction")
@CjService(name = "billService")
public class BillService implements IBillService {
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.AbsorbBillMapper")
    AbsorbBillMapper absorbBillMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.BalanceBillMapper")
    BalanceBillMapper balanceBillMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.OnorderBillMapper")
    OnorderBillMapper onorderBillMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.FreezenBillMapper")
    FreezenBillMapper freezenBillMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.ProfitBillMapper")
    ProfitBillMapper profitBillMapper;
    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.WenyBillMapper")
    WenyBillMapper wenyBillMapper;
    @CjServiceRef
    IWalletService walletService;

    @CjTransaction
    @Override
    public List<AbsorbBill> pageAbsorbBill(String person, int limit, long offset) {
        AbsorbAccount absorbAccount = walletService.getAbsorbAccount(person);
        return absorbBillMapper.page(absorbAccount.getId(), limit, offset);
    }

    @CjTransaction
    @Override
    public List<AbsorbBill> monthAbsorbBill(String person, int year, int month, int limit, long offset) {
        AbsorbAccount absorbAccount = walletService.getAbsorbAccount(person);
        return absorbBillMapper.month(absorbAccount.getId(), year, month, limit, offset);
    }

    @CjTransaction
    @Override
    public List<BalanceBill> pageBalanceBill(String person, int limit, long offset) {
        BalanceAccount balanceAccount = walletService.getBalanceAccount(person);
        return balanceBillMapper.page(balanceAccount.getId(), limit, offset);
    }
    @CjTransaction
    @Override
    public List<BalanceBill> pageBalanceBillByOrder(String person, int order, int limit, long offset) {
        BalanceAccount balanceAccount = walletService.getBalanceAccount(person);
        return balanceBillMapper.pageByOrder(balanceAccount.getId(),order, limit, offset);
    }

    @CjTransaction
    @Override
    public List<BalanceBill> monthBalanceBill(String person, int year, int month, int limit, long offset) {
        BalanceAccount balanceAccount = walletService.getBalanceAccount(person);
        return balanceBillMapper.month(balanceAccount.getId(), year, month, limit, offset);
    }

    @CjTransaction
    @Override
    public List<OnorderBill> pageOnorderBill(String person, int limit, long offset) {
        RootAccount rootAccount = walletService.getRootAccount(person);
        return onorderBillMapper.page(rootAccount.getId(), limit, offset);
    }

    @CjTransaction
    @Override
    public List<OnorderBill> monthOnorderBill(String person, int year, int month, int limit, long offset) {
        RootAccount rootAccount = walletService.getRootAccount(person);
        return onorderBillMapper.month(rootAccount.getId(), year, month, limit, offset);
    }

    @CjTransaction
    @Override
    public List<FreezenBill> pageFreezenBill(String person, String wenyBankID, int limit, long offset) {
        FreezenAccount freezenAccount = walletService.getFreezenAccount(person, wenyBankID);
        return freezenBillMapper.page(freezenAccount.getId(), wenyBankID, limit, offset);
    }

    @CjTransaction
    @Override
    public List<FreezenBill> monthFreezenBill(String person, String wenyBankID, int year, int month, int limit, long offset) {
        FreezenAccount freezenAccount = walletService.getFreezenAccount(person, wenyBankID);
        return freezenBillMapper.month(freezenAccount.getId(), wenyBankID, year, month, limit, offset);
    }

    @CjTransaction
    @Override
    public List<ProfitBill> pageProfitBill(String person, String wenyBankID, int limit, long offset) {
        ProfitAccount profitAccount = walletService.getProfitAccount(person, wenyBankID);
        return profitBillMapper.page(profitAccount.getId(), wenyBankID, limit, offset);
    }

    @CjTransaction
    @Override
    public List<ProfitBill> monthProfitBill(String person, String wenyBankID, int year, int month, int limit, long offset) {
        ProfitAccount profitAccount = walletService.getProfitAccount(person, wenyBankID);
        return profitBillMapper.month(profitAccount.getId(), wenyBankID, year, month, limit, offset);
    }

    @CjTransaction
    @Override
    public List<WenyBill> pageStockBill(String person, String wenyBankID, int limit, long offset) {
        WenyAccount wenyAccount = walletService.getWenyAccount(person, wenyBankID);
        return wenyBillMapper.page(wenyAccount.getId(), wenyBankID, limit, offset);
    }

    @CjTransaction
    @Override
    public List<WenyBill> monthStockBill(String person, String wenyBankID, int year, int month, int limit, long offset) {
        WenyAccount wenyAccount = walletService.getWenyAccount(person, wenyBankID);
        return wenyBillMapper.month(wenyAccount.getId(), wenyBankID, year, month, limit, offset);
    }
}
