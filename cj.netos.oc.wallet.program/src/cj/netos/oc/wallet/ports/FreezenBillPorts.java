package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IBillService;
import cj.netos.oc.wallet.model.AbsorbBill;
import cj.netos.oc.wallet.model.FreezenBill;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/bill/freezen.ports")
public class FreezenBillPorts implements IFreezenBillPorts {
    @CjServiceRef
    IBillService billService;

    @Override
    public List<FreezenBill> pageBill(ISecuritySession securitySession, String person,String wenyBankID, int limit, long offset) throws CircuitException {
        return billService.pageFreezenBill(person,wenyBankID, limit, offset);
    }

    @Override
    public List<FreezenBill> monthBill(ISecuritySession securitySession, String person,String wenyBankID, int year, int month, int limit, long offset) throws CircuitException {
        return billService.monthFreezenBill(person,wenyBankID, year, month, limit, offset);
    }
}
