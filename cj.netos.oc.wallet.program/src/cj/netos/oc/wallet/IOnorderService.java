package cj.netos.oc.wallet;

import cj.netos.oc.wallet.bo.OnorderBO;
import cj.studio.ecm.net.CircuitException;
import org.checkerframework.checker.units.qual.C;

public interface IOnorderService {
    void put(OnorderBO onorderBO) throws CircuitException;

    void returnOrder(OnorderBO onorderBO) throws CircuitException;

    void done(OnorderBO onorderBO) throws CircuitException;
}
