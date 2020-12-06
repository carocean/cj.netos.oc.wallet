package cj.netos.oc.wallet.ports;

import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.model.*;
import cj.netos.oc.wallet.program.ICuratorPathChecker;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.Map;

@CjService(name = "/balance.ports")
public class BalancePorts implements IBalancePorts {
    @CjServiceRef
    IWalletService walletService;
    @CjServiceRef(refByName = "curator.framework")
    CuratorFramework framework;

    @CjServiceRef
    ICuratorPathChecker curatorPathChecker;

    @Override
    public Map<String, Object> getAllAccount(ISecuritySession securitySession, String person) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getAllAccount(person);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public RootAccount getRootAccount(ISecuritySession securitySession, String person) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getRootAccount(person);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public BalanceAccount getBalanceAccount(ISecuritySession securitySession, String person) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getBalanceAccount(person);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public AbsorbAccount getAbsorbAccount(ISecuritySession securitySession, String person) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getAbsorbAccount(person);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public TrialAccount getTrialAccount(ISecuritySession securitySession, String person) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getTrialAccount(person);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public FreezenAccount getFreezenAccount(ISecuritySession securitySession, String person,String bankid) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getFreezenAccount(person,bankid);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public ProfitAccount getProfitAccount(ISecuritySession securitySession, String person, String bankid) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getProfitAccount(person,bankid);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public Map<String, Object> getWenyAccounts(ISecuritySession securitySession, String person) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getWenyAccounts(person);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public WenyAccount getStockAccount(ISecuritySession securitySession, String person, String wenyBankID) throws CircuitException {
        String path = String.format("/wallet/%s/locks", person);
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(framework, path);
        InterProcessMutex mutex = lock.writeLock();
        try {
            curatorPathChecker.check(framework, path);
        } catch (Exception e) {
            throw new CircuitException("500", e);
        }
        try {
            mutex.acquire();
            return walletService.getWenyAccount(person,wenyBankID);
        } catch (CircuitException e) {
            throw e;
        } catch (Exception e) {
            throw new CircuitException("500", e);
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
