package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.PurchaseBO;
import cj.netos.oc.wallet.bo.PurchasedBO;
import cj.netos.oc.wallet.result.PurchaseResult;
import cj.netos.oc.wallet.result.PurchasingResult;
import cj.studio.ecm.net.CircuitException;

public interface IPurchaseActivityController {
    PurchasingResult receipt(PurchaseBO bo) throws CircuitException;

    void settle(PurchasedBO purchasedBO) throws CircuitException;


    void sendReceiptAck(PurchaseResult result) throws CircuitException;


    void sendSettleAck(PurchaseResult result) throws CircuitException;

}
