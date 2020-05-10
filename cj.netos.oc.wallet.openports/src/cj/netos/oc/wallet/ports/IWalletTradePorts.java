package cj.netos.oc.wallet.ports;

import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenports;

@CjOpenports(usage = "钱包交易指令开放api")
public interface IWalletTradePorts extends IOpenportService {
    void payment(
            String payer,
            String payee,
            String type,
            long amount
                 ) throws CircuitException;

    void recharge() throws CircuitException;

    void withdraw() throws CircuitException;

    void transfer() throws CircuitException;

    void queryAmount() throws CircuitException;

    void gathering() throws CircuitException;

}
