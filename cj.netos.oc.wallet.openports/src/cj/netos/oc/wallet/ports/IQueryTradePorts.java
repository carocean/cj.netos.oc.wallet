package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.TradeBO;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenports;

import java.util.List;

@CjOpenports(usage = "交易记录查询开放api")
public interface IQueryTradePorts extends IOpenportService {
    List<TradeBO> pageAnyTrade() throws CircuitException;

    List<TradeBO> pageOrderTrade() throws CircuitException;

    TradeBO getTrade() throws CircuitException;
}
