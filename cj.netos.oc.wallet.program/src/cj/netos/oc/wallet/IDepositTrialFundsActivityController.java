package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.DepositTrialBO;
import cj.netos.oc.wallet.result.DepositTrialFundsResult;
import cj.studio.ecm.net.CircuitException;

public interface IDepositTrialFundsActivityController {
    void doReceipt(DepositTrialBO depositTrialBO);

    void sendReceiptAck(DepositTrialFundsResult result)throws CircuitException;

}
