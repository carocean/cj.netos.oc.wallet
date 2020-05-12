package cj.netos.oc.wallet.ports;

import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenports;

import java.util.Map;

@CjOpenports(usage = "会计相关开放api")
public interface IFinancePorts extends IOpenportService {
    @CjOpenport(usage = "获取当前会计日期")
    Map<String, Object> getCurrentWorkday(ISecuritySession securitySession
    ) throws CircuitException;
}
