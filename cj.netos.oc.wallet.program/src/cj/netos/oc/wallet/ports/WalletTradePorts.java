package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IWalletTradeService;
import cj.netos.oc.wallet.bo.RechargeBO;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;
import cj.ultimate.gson2.com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

@CjService(name = "/trade.ports")
public class WalletTradePorts implements IWalletTradePorts {

    @CjServiceRef
    IWalletTradeService walletTradeService;

    @Override
    public Map<String, Object> rechargeOrder(ISecuritySession securitySession, RechargeBO recharge) throws CircuitException {
        Object obj = walletTradeService.addRechargeOrder(recharge);
        String json = new Gson().toJson(obj);
        return new Gson().fromJson(json, HashMap.class);
    }

    @Override
    public void rechargeDone(ISecuritySession securitySession, String sn, long amount,String code,String message) throws CircuitException {
        walletTradeService.rechargeDone(sn,amount,code,message);
    }

    @Override
    public void withdraw(ISecuritySession securitySession) throws CircuitException {
        System.out.println(securitySession.principal());
    }

    @Override
    public void exchange(ISecuritySession securitySession) throws CircuitException {

    }

    @Override
    public void purchase(ISecuritySession securitySession) throws CircuitException {

    }
}
