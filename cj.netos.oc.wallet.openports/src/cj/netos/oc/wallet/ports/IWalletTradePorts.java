package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.bo.PurchaseBO;
import cj.netos.oc.wallet.bo.RechargeBO;
import cj.netos.oc.wallet.bo.WithdrawBO;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.AccessTokenIn;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.PKeyInRequest;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportAppSecurity;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.Map;

@CjOpenports(usage = "钱包交易指令开放api")
public interface IWalletTradePorts extends IOpenportService {

//    @CjOpenportAppSecurity
//    @CjOpenport(usage = "充值收单交易指令", tokenIn = AccessTokenIn.nope, command = "post")
//    Map<String, Object> rechargeOrder(ISecuritySession securitySession,
//                                      @CjOpenportParameter(usage = "充值单", name = "rechargeBill", in = PKeyInRequest.content)
//                                              RechargeBO rechargeBill) throws CircuitException;
//
//    @CjOpenportAppSecurity
//    @CjOpenport(usage = "完成充值的交易指令", tokenIn = AccessTokenIn.nope)
//    void rechargeDone(ISecuritySession securitySession,
//                      @CjOpenportParameter(usage = "订单号", name = "sn") String sn,
//                      @CjOpenportParameter(usage = "实际完成充值的金额,单位为分", name = "amount") long amount,
//                      @CjOpenportParameter(usage = "订单完成时第三方渠道的返回码", name = "code") String code,
//                      @CjOpenportParameter(usage = "订单完成时第三方渠道的返回信息", name = "message") String message
//    ) throws CircuitException;
//
//    @CjOpenportAppSecurity
//    @CjOpenport(usage = "提现收单交易指令", tokenIn = AccessTokenIn.nope, command = "post")
//    Map<String, Object> withdrawOrder(ISecuritySession securitySession,
//                                      @CjOpenportParameter(usage = "提现单", name = "withdrawBill", in = PKeyInRequest.content)
//                                              WithdrawBO withdrawBill) throws CircuitException;
//
//    @CjOpenportAppSecurity
//    @CjOpenport(usage = "完成提现的交易指令", tokenIn = AccessTokenIn.nope)
//    void withdrawDone(ISecuritySession securitySession,
//                      @CjOpenportParameter(usage = "订单号", name = "sn") String sn,
//                      @CjOpenportParameter(usage = "实际完成提现的金额,单位为分", name = "amount") long amount,
//                      @CjOpenportParameter(usage = "订单完成时第三方渠道的返回码", name = "code") String code,
//                      @CjOpenportParameter(usage = "订单完成时第三方渠道的返回信息", name = "message") String message
//    ) throws CircuitException;
//
//    @CjOpenportAppSecurity
//    @CjOpenport(usage = "申购收单", tokenIn = AccessTokenIn.nope)
//    Map<String,Object>  purchaseWenyOrder(ISecuritySession securitySession,
//                           @CjOpenportParameter(usage = "申购单", name = "purchaseBill", in = PKeyInRequest.content)
//                                   PurchaseBO purchaseBO) throws CircuitException;
//
//
//    @CjOpenportAppSecurity
//    @CjOpenport(usage = "承兑收单", tokenIn = AccessTokenIn.nope)
//    void exchangeWenyOrder(ISecuritySession securitySession) throws CircuitException;


}
