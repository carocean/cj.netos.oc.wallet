package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.ModuleTransinBO;
import cj.netos.oc.wallet.result.ModuleTransinResult;
import cj.studio.ecm.net.CircuitException;

public interface IModuleTransinActivityController {
    void doReceipt(ModuleTransinBO moduleTransinBO)throws CircuitException;

    void sendReceiptAck(ModuleTransinResult result)throws CircuitException;

}
