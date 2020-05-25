package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.RechargeBO;
import cj.netos.oc.wallet.result.RechargeResult;
import cj.studio.ecm.net.CircuitException;
import cj.studio.orm.mybatis.annotation.CjTransaction;

public interface IRechargeActivityController {
    void settle(RechargeBO rechargeBO) throws CircuitException;

    @CjTransaction
    void sendSettleAck(RechargeResult result) throws CircuitException;
}
