package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.Person;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;
import cj.ultimate.gson2.com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

@CjService(name = "/wallet.ports")
public class WalletPorts implements IWalletPorts {
    @CjServiceRef
    IWalletService walletService;

    @Override
    public Map<String, Object> initWallet(ISecuritySession securitySession, Person person) throws CircuitException {
        return walletService.initWallet(person);
    }

    @Override
    public boolean isinitWallet(ISecuritySession securitySession, String person) throws CircuitException {
        return walletService.isinitWallet(person);
    }

    @Override
    public Map<String, Object> getAllAccount(ISecuritySession securitySession, String person) throws CircuitException {
        return walletService.getAllAccount(person);
    }

    @Override
    public Map<String, Object> getBalanceAccount(ISecuritySession securitySession, String person) throws CircuitException {
        Object obj = walletService.getBalanceAccount(person);
        return new Gson().fromJson(new Gson().toJson(obj), HashMap.class);
    }
}
