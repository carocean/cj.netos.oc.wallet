package cj.netos.oc.wallet;

import cj.netos.oc.wallet.model.*;

import java.util.List;

public interface IBillService {
    List<AbsorbBill> pageAbsorbBill(String person, int limit, long offset);

    List<AbsorbBill> monthAbsorbBill(String person, int year, int month, int limit, long offset);

    List<BalanceBill> pageBalanceBill(String person, int limit, long offset);

    List<BalanceBill> monthBalanceBill(String person, int year, int month, int limit, long offset);

    List<OnorderBill> pageOnorderBill(String person, int limit, long offset);

    List<OnorderBill> monthOnorderBill(String person, int year, int month, int limit, long offset);

    List<FreezenBill> pageFreezenBill(String person, String wenyBankID, int limit, long offset);

    List<FreezenBill> monthFreezenBill(String person, String wenyBankID, int year, int month, int limit, long offset);

    List<ProfitBill> pageProfitBill(String person, String wenyBankID, int limit, long offset);

    List<ProfitBill> monthProfitBill(String person, String wenyBankID, int year, int month, int limit, long offset);

    List<WenyBill> pageStockBill(String person, String wenyBankID, int limit, long offset);

    List<WenyBill> monthStockBill(String person, String wenyBankID, int year, int month, int limit, long offset);

    List<BalanceBill> pageBalanceBillByOrder(String person, int order, int limit, long offset);

    List<TrialBill> pageTrialBill(String person, int limit, long offset);

    List<TrialBill> monthTrialBill(String person, int year, int month, int limit, long offset);

}
