package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.RechargeBO;
import cj.netos.oc.wallet.bo.WithdrawBO;
import cj.studio.ecm.net.CircuitException;

public interface ISettleTradeService {



    void withdraw(WithdrawBO withdrawBO) throws CircuitException;


    void recharge(RechargeBO rechargeBO)throws CircuitException;

}
