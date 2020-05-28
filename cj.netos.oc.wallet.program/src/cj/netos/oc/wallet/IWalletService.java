package cj.netos.oc.wallet;

import cj.netos.oc.wallet.model.*;
import cj.studio.orm.mybatis.annotation.CjTransaction;

import java.util.List;
import java.util.Map;

public interface IWalletService {

    void createWenyBankAccount(String person, String personName, String bankid);


    boolean hasWenyBankAccount(String person, String bankid);

    Map<String, Object> createWallet(String person, String personName);


    Map<String, Object> getWenyAccounts(String person);


    List<WenyAccount> listWenyAccount(String person);


    WenyAccount getWenyAccount(String person, String bankid);


    AbsorbAccount getAbsorbAccount(String person);


    List<ProfitAccount> listProfitAccount(String person);


    ProfitAccount getProfitAccount(String person, String bankid);

    List<FreezenAccount> listFreezenAccount(String person);


    FreezenAccount getFreezenAccount(String person, String bankid);

    BalanceAccount getBalanceAccount(String person);


    RootAccount getRootAccount(String person);

    boolean hasWallet(String person);

    Map<String, Object> getAllAccount(String person);


}
