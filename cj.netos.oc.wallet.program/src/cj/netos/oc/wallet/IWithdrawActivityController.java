package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.WithdrawBO;
import cj.netos.oc.wallet.result.WithdrawResult;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;

public interface IWithdrawActivityController {
    @CjTransaction
    void cancelReceipt(WithdrawBO withdrawBO) throws CircuitException;

    void sendReceiptAck(WithdrawResult result) throws CircuitException;

    @CjTransaction
    void sendCancelReceiptAck(WithdrawResult result) throws CircuitException;

    void settle(WithdrawBO withdrawBO) throws CircuitException;

    void doReceipt(WithdrawBO withdrawBO) throws CircuitException;

    @CjTransaction
    void sendSettleAck(WithdrawResult result) throws CircuitException;
}
