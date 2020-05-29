package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IBillService;
import cj.netos.oc.wallet.model.AbsorbBill;
import cj.netos.oc.wallet.model.OnorderBill;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/bill/onorder.ports")
public class OnorderBillPorts implements IOnorderBillPorts {
    @CjServiceRef
    IBillService billService;

    @Override
    public List<OnorderBill> pageBill(ISecuritySession securitySession, String person, int limit, long offset) throws CircuitException {
        return billService.pageOnorderBill(person, limit, offset);
    }

    @Override
    public List<OnorderBill> monthBill(ISecuritySession securitySession, String person, int year, int month, int limit, long offset) throws CircuitException {
        return billService.monthOnorderBill(person, year, month, limit, offset);
    }
}
