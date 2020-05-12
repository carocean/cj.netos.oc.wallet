package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.Person;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.AccessTokenIn;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.PKeyInRequest;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportAppSecurity;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.HashMap;
import java.util.Map;

@CjOpenports(usage = "钱包开放api")
public interface IWalletPorts extends IOpenportService {

    @CjOpenportAppSecurity
    @CjOpenport(usage = "开通钱包账户", tokenIn = AccessTokenIn.nope, command = "post")
    Map<String, Object> initWallet(ISecuritySession securitySession,
                                   @CjOpenportParameter(usage = "要开通钱包的用户信息", name = "person", in = PKeyInRequest.content) Person person
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "是否开通了钱包账户", tokenIn = AccessTokenIn.nope)
    boolean isinitWallet(ISecuritySession securitySession,
                         @CjOpenportParameter(usage = "公众号", name = "person") String person
    ) throws CircuitException;

    @CjOpenportAppSecurity
    @CjOpenport(usage = "获取各账户信息", tokenIn = AccessTokenIn.nope)
    Map<String, Object> getAllAccounts(ISecuritySession securitySession,
                                       @CjOpenportParameter(usage = "公众号", name = "person") String person
    ) throws CircuitException;
}
