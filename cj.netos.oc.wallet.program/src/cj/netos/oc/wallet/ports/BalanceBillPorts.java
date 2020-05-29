package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IBillService;
import cj.netos.oc.wallet.model.AbsorbBill;
import cj.netos.oc.wallet.model.BalanceBill;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/bill/balance.ports")
public class BalanceBillPorts implements IBalanceBillPorts {
    @CjServiceRef
    IBillService billService;

    @Override
    public List<BalanceBill> pageBill(ISecuritySession securitySession, String person, int limit, long offset) throws CircuitException {
        return billService.pageBalanceBill(person, limit, offset);
    }

    @Override
    public List<BalanceBill> monthBill(ISecuritySession securitySession, String person, int year, int month, int limit, long offset) throws CircuitException {
        return billService.monthBalanceBill(person, year, month, limit, offset);
    }
}
