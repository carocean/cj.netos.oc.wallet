package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.TransProfitBO;
import cj.netos.oc.wallet.result.TransProfitResult;
import cj.studio.ecm.net.CircuitException;

public interface ITransProfitActivityController {
    void doReceipt(TransProfitBO transProfitBO)throws CircuitException;

    void sendReceiptAck(TransProfitResult result)throws CircuitException;

}
