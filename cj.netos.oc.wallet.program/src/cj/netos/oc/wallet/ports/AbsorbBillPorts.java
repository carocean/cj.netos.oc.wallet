package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IBillService;
import cj.netos.oc.wallet.model.AbsorbBill;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/bill/absorb.ports")
public class AbsorbBillPorts implements IAbsorbBillPorts {
    @CjServiceRef
    IBillService billService;

    @Override
    public List<AbsorbBill> pageBill(ISecuritySession securitySession, String person, int limit, long offset) throws CircuitException {
        return billService.pageAbsorbBill(person, limit, offset);
    }

    @Override
    public List<AbsorbBill> monthBill(ISecuritySession securitySession, String person, int year, int month, int limit, long offset) throws CircuitException {
        return billService.monthAbsorbBill(person, year, month, limit, offset);
    }
}
