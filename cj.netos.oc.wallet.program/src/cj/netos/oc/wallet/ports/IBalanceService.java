package cj.netos.oc.wallet.ports;

import cj.studio.ecm.net.CircuitException;

public interface IBalanceService {
    void addFundBalance() throws CircuitException;

    void decFundBalance() throws CircuitException;

    void queryFundBalance() throws CircuitException;

    void addWenyBalance() throws CircuitException;

    void decWenyBalance() throws CircuitException;

    void queryWenyBalance() throws CircuitException;
}
