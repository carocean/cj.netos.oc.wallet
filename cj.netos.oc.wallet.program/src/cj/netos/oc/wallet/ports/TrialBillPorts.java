package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IBillService;
import cj.netos.oc.wallet.model.AbsorbBill;
import cj.netos.oc.wallet.model.TrialBill;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/bill/trial.ports")
public class TrialBillPorts implements ITrialBillPorts {
    @CjServiceRef
    IBillService billService;

    @Override
    public List<TrialBill> pageBill(ISecuritySession securitySession, String person, int limit, long offset) throws CircuitException {
        return billService.pageTrialBill(person, limit, offset);
    }

    @Override
    public List<TrialBill> monthBill(ISecuritySession securitySession, String person, int year, int month, int limit, long offset) throws CircuitException {
        return billService.monthTrialBill(person, year, month, limit, offset);
    }
}
