package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.*;
import cj.netos.oc.wallet.result.ExchangeResult;
import cj.studio.ecm.net.CircuitException;

public interface ISettleTradeService {



    void withdraw(WithdrawBO withdrawBO) throws CircuitException;


    void recharge(RechargeBO rechargeBO)throws CircuitException;

    void purchase(PurchasedBO purchasedBO) throws CircuitException;

    ExchangeResult exchange(ExchangedBO bo);

    void depositAbsorb(DepositAbsorbBO depositAbsorbBO);

    void transAbsorb(TransAbsorbBO transAbsorbBO) throws CircuitException;

//    void transProfit(TransProfitBO transAbsorbBO) throws CircuitException;

    void transShunt(WithdrawShunterBO transShuntedBO);

    void payTrade(PayBO payBO) throws CircuitException;

    void p2p(P2PBO pbo) throws CircuitException;

    void depositHubTails(DepositHubTailsBO depositAbsorbBO);

    void depositTrialFunds(DepositTrialBO depositTrialBO);

    void moduleTransin(ModuleTransinBO moduleTransinBO);

}
