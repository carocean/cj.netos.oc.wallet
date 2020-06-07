package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.DepositAbsorbBO;
import cj.netos.oc.wallet.result.DepositAbsorbResult;
import cj.studio.ecm.net.CircuitException;

public interface IDepositAbsorbActivityController {
    void doReceipt(DepositAbsorbBO depositAbsorbBO)throws CircuitException;

    void sendReceiptAck(DepositAbsorbResult result)throws CircuitException;

}
