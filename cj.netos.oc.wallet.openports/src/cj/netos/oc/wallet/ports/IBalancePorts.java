package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.model.*;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.AccessTokenIn;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportAppSecurity;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.Map;

@CjOpenports(usage = "余额类服务")
public interface IBalancePorts extends IOpenportService {

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取各账户余额", tokenIn = AccessTokenIn.nope)
    Map<String, Object> getAllAccount(ISecuritySession securitySession,
                                      @CjOpenportParameter(usage = "用户", name = "person") String person
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取主账户余额", tokenIn = AccessTokenIn.nope)
    RootAccount getRootAccount(ISecuritySession securitySession,
                               @CjOpenportParameter(usage = "用户", name = "person") String person
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取零钱账户余额", tokenIn = AccessTokenIn.nope)
    BalanceAccount getBalanceAccount(ISecuritySession securitySession,
                                     @CjOpenportParameter(usage = "用户", name = "person") String person
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取洇金账户余额", tokenIn = AccessTokenIn.nope)
    AbsorbAccount getAbsorbAccount(ISecuritySession securitySession,
                                   @CjOpenportParameter(usage = "用户", name = "person") String person
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取体验金账户余额", tokenIn = AccessTokenIn.nope)
    TrialAccount getTrialAccount(ISecuritySession securitySession,
                                   @CjOpenportParameter(usage = "用户", name = "person") String person
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取冻结账户余额", tokenIn = AccessTokenIn.nope)
    FreezenAccount getFreezenAccount(ISecuritySession securitySession,
                                     @CjOpenportParameter(usage = "用户", name = "person") String person,
                                     @CjOpenportParameter(usage = "纹银银行行号", name = "wenyBankID") String wenyBankID
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取收益账户余额", tokenIn = AccessTokenIn.nope)
    ProfitAccount getProfitAccount(ISecuritySession securitySession,
                                   @CjOpenportParameter(usage = "用户", name = "person") String person,
                                   @CjOpenportParameter(usage = "纹银银行行号", name = "wenyBankID") String wenyBankID
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取访问者的纹银关联账户余额。包括，纹银账户、冻结账户、收益账户", tokenIn = AccessTokenIn.nope)
    Map<String, Object> getWenyAccounts(ISecuritySession securitySession,
                                        @CjOpenportParameter(usage = "用户", name = "person") String person
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取纹银账户余额", tokenIn = AccessTokenIn.nope)
    WenyAccount getStockAccount(ISecuritySession securitySession,
                                @CjOpenportParameter(usage = "用户", name = "person") String person,
                                @CjOpenportParameter(usage = "纹银银行行号", name = "wenyBankID") String wenyBankID
    ) throws CircuitException;


}
