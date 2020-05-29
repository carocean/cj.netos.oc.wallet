package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IBillService;
import cj.netos.oc.wallet.model.ProfitBill;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/bill/profit.ports")
public class ProfitBillPorts implements IProfitBillPorts {
    @CjServiceRef
    IBillService billService;

    @Override
    public List<ProfitBill> pageBill(ISecuritySession securitySession, String person,String wenyBankID, int limit, long offset) throws CircuitException {
        return billService.pageProfitBill(person,wenyBankID, limit, offset);
    }

    @Override
    public List<ProfitBill> monthBill(ISecuritySession securitySession, String person,String wenyBankID, int year, int month, int limit, long offset) throws CircuitException {
        return billService.monthProfitBill(person,wenyBankID, year, month, limit, offset);
    }
}
