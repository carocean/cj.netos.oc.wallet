package cj.netos.oc.wallet;

import cj.netos.oc.wallet.model.*;
import cj.studio.orm.mybatis.annotation.CjTransaction;

import java.util.Map;

public interface IWalletService {
    Map<String, Object> initWallet(Person person);

    @CjTransaction
    WenyAccount getWenyAccount(String person);

    @CjTransaction
    AbsorbAccount getAbsorbAccount(String person);

    @CjTransaction
    ProfitAccount getProfitAccount(String person);

    @CjTransaction
    FreezenAccount getFreezenAccount(String person);

    @CjTransaction
    BalanceAccount getBalanceAccount(String person);

    @CjTransaction
    RootAccount getRootAccount(String person);

    boolean isinitWallet(String person);

    Map<String, Object> getAllAccount(String person);


}