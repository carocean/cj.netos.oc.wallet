package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.RechargeBO;
import cj.netos.oc.wallet.model.RechargeRecord;
import cj.studio.ecm.net.CircuitException;

public interface IWalletTradeService {
    RechargeRecord addRechargeOrder(RechargeBO recharge) throws CircuitException;

    void rechargeDone(String sn, long amount,String code,String message) throws CircuitException;

}
