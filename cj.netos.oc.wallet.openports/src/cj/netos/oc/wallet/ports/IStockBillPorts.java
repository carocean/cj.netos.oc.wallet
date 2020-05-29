package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.model.AbsorbBill;
import cj.netos.oc.wallet.model.WenyBill;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.AccessTokenIn;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportAppSecurity;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.List;

@CjOpenports(usage = "纹银出入账单")
public interface IStockBillPorts extends IOpenportService {
    @CjOpenportAppSecurity
    @CjOpenport(usage = "分页账单，倒序",tokenIn = AccessTokenIn.nope)
    List<WenyBill> pageBill(ISecuritySession securitySession,
                            @CjOpenportParameter(usage = "公众", name = "person") String person,
                            @CjOpenportParameter(usage = "行号", name = "wenyBankID") String wenyBankID,
                            @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
                            @CjOpenportParameter(usage = "偏移", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "月账单，倒序",tokenIn = AccessTokenIn.nope)
    List<WenyBill> monthBill(ISecuritySession securitySession,
                               @CjOpenportParameter(usage = "公众", name = "person") String person,
                             @CjOpenportParameter(usage = "行号", name = "wenyBankID") String wenyBankID,
                               @CjOpenportParameter(usage = "年", name = "year") int year,
                               @CjOpenportParameter(usage = "月.（java特性，实际用份减1）", name = "month") int month,
                               @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
                               @CjOpenportParameter(usage = "偏移", name = "offset") long offset
    ) throws CircuitException;
}
