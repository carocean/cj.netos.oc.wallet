package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.DepositHubTailsBO;
import cj.netos.oc.wallet.result.DepositHubTailsResult;
import cj.studio.ecm.net.CircuitException;

public interface IDepositHubTailsActivityController {
    void doReceipt(DepositHubTailsBO depositAbsorbBO) throws CircuitException;

    void sendReceiptAck(DepositHubTailsResult result) throws CircuitException;

}
