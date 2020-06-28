package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.TransShuntBO;
import cj.netos.oc.wallet.bo.WithdrawShunterBO;
import cj.netos.oc.wallet.result.TransShuntResult;
import cj.netos.oc.wallet.result.WithdrawShunterResult;
import cj.studio.ecm.net.CircuitException;

public interface ITransShunterActivityController {
    WithdrawShunterResult receipt(TransShuntBO transShuntBO)throws CircuitException;

    void sendReceiptAck(TransShuntResult result)throws CircuitException;

    void settle(WithdrawShunterBO transShuntedBO)throws CircuitException;

    void sendSettleAck(TransShuntResult result)throws CircuitException;

}
