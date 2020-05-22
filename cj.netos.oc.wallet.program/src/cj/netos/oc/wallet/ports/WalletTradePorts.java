package cj.netos.oc.wallet.ports;

import cj.studio.ecm.annotation.CjService;

@CjService(name = "/trade.ports")
public class WalletTradePorts implements IWalletTradePorts {

//    @CjServiceRef
//    IWalletTradeService walletTradeService;
//
//    @Override
//    public Map<String, Object> rechargeOrder(ISecuritySession securitySession, RechargeBO recharge) throws CircuitException {
//        Object obj = walletTradeService.addRechargeOrder(recharge);
//        String json = new Gson().toJson(obj);
//        return new Gson().fromJson(json, HashMap.class);
//    }
//
//    @Override
//    public void rechargeDone(ISecuritySession securitySession, String sn, long amount,String code,String message) throws CircuitException {
//        walletTradeService.rechargeDone(sn,amount,code,message);
//    }
//
//    @Override
//    public Map<String, Object> withdrawOrder(ISecuritySession securitySession, WithdrawBO withdrawBO) throws CircuitException {
//        Object obj = walletTradeService.addWithdrawOrder(withdrawBO);
//        String json = new Gson().toJson(obj);
//        return new Gson().fromJson(json, HashMap.class);
//    }
//
//    @Override
//    public void withdrawDone(ISecuritySession securitySession, String sn, long amount, String code, String message) throws CircuitException {
//        walletTradeService.withdrawDone(sn,amount,code,message);
//    }
//
//    @Override
//    public Map<String,Object> purchaseWenyOrder(ISecuritySession securitySession, PurchaseBO purchaseBO) throws CircuitException {
//        Object obj = walletTradeService.addPurchaseeOrder(purchaseBO);
//        String json = new Gson().toJson(obj);
//        return new Gson().fromJson(json, HashMap.class);
//    }
//
//    @Override
//    public void exchangeWenyOrder(ISecuritySession securitySession) throws CircuitException {
//
//    }

}
