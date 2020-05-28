package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.ExchangeBO;
import cj.netos.oc.wallet.bo.ExchangedBO;
import cj.netos.oc.wallet.result.ExchangeResult;
import cj.netos.oc.wallet.result.ExchangingResult;
import cj.studio.ecm.net.CircuitException;

public interface IExchangeActivityController {
    ExchangeResult doReceipt(ExchangeBO bo) throws CircuitException;

    void sendReceiptAck(ExchangingResult result) throws CircuitException;

    ExchangeResult settle(ExchangedBO bo)throws CircuitException;

    void sendSettleAck(ExchangingResult result)throws CircuitException;

}
