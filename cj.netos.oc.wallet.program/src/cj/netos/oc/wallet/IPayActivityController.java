package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.PayBO;
import cj.netos.oc.wallet.result.PayResult;
import cj.studio.ecm.net.CircuitException;

public interface IPayActivityController {
    void doReceipt(PayBO payBO)throws CircuitException;

    void sendReceiptAck(PayResult result)throws CircuitException;

}
