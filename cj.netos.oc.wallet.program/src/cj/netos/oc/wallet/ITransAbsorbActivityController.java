package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.TransAbsorbBO;
import cj.netos.oc.wallet.result.TransAbsorbResult;
import cj.studio.ecm.net.CircuitException;

public interface ITransAbsorbActivityController {
    void doReceipt(TransAbsorbBO transAbsorbBO)throws CircuitException;

    void sendReceiptAck(TransAbsorbResult result)throws CircuitException;

}
