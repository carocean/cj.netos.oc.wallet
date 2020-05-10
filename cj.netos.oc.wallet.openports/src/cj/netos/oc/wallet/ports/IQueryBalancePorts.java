package cj.netos.oc.wallet.ports;

import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.annotations.CjOpenports;

//余额更新记入交易记录
@CjOpenports(usage = "余额开放api")
public interface IQueryBalancePorts extends IOpenportService {

    void queryFundBalance() throws CircuitException;

    void queryWenyBalance() throws CircuitException;
}
