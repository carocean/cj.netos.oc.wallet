package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.P2PBO;
import cj.netos.oc.wallet.result.P2PResult;
import cj.studio.ecm.net.CircuitException;

public interface IP2PActivityController {
    void doReceipt(P2PBO pbo)throws CircuitException;

    void sendReceiptAck(P2PResult result)throws CircuitException;

}
