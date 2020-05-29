package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IBillService;
import cj.netos.oc.wallet.model.WenyBill;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/bill/stock.ports")
public class StockBillPorts implements IStockBillPorts {
    @CjServiceRef
    IBillService billService;

    @Override
    public List<WenyBill> pageBill(ISecuritySession securitySession, String person,String wenyBankID, int limit, long offset) throws CircuitException {
        return billService.pageStockBill(person, wenyBankID, limit, offset);
    }

    @Override
    public List<WenyBill> monthBill(ISecuritySession securitySession, String person,String wenyBankID, int year, int month, int limit, long offset) throws CircuitException {
        return billService.monthStockBill(person, wenyBankID, year, month, limit, offset);
    }
}
