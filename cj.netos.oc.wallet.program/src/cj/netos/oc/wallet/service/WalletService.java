package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.mapper.*;
import cj.netos.oc.wallet.model.*;
import cj.netos.oc.wallet.util.IdWorker;
import cj.netos.oc.wallet.util.WalletUtils;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.openport.util.Encript;
import cj.studio.orm.mybatis.annotation.CjTransaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CjBridge(aspects = "@transaction")
@CjService(name = "walletService")
public class WalletService implements IWalletService {

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.RootAccountMapper")
    RootAccountMapper rootAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.BalanceAccountMapper")
    BalanceAccountMapper balanceAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.FreezenAccountMapper")
    FreezenAccountMapper freezenAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.ProfitAccountMapper")
    ProfitAccountMapper profitAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.AbsorbAccountMapper")
    AbsorbAccountMapper absorbAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.WenyAccountMapper")
    WenyAccountMapper wenyAccountMapper;

    @CjServiceRef(refByName = "mybatis.cj.netos.oc.wallet.mapper.FeeAccountMapper")
    FeeAccountMapper feeAccountMapper;

    @CjTransaction
    @Override
    public void createWenyBankAccount(String person, String personName, String bankid) {
        if (hasWenyBankAccount(person, bankid)) {
            return;
        }

        WenyAccount wenyAccount = new WenyAccount();
        wenyAccount.setStock(BigDecimal.ZERO);
        wenyAccount.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        wenyAccount.setCurrency("WENY");
        wenyAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        wenyAccount.setLutime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        wenyAccount.setPerson(person);
        wenyAccount.setPersonName(personName);
        wenyAccount.setState(0);
        wenyAccount.setBankid(bankid);
        wenyAccountMapper.insert(wenyAccount);

        FreezenAccount freezenAccount = new FreezenAccount();
        freezenAccount.setAmount(0L);
        freezenAccount.setCtime(wenyAccount.getCtime());
        freezenAccount.setCurrency("CNY");
        freezenAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        freezenAccount.setLutime(wenyAccount.getLutime());
        freezenAccount.setPerson(person);
        freezenAccount.setPersonName(personName);
        freezenAccount.setState(0);
        freezenAccount.setBankid(bankid);
        freezenAccountMapper.insert(freezenAccount);

        ProfitAccount profitAccount = new ProfitAccount();
        profitAccount.setAmount(0L);
        profitAccount.setCtime(wenyAccount.getCtime());
        profitAccount.setCurrency("CNY");
        profitAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        profitAccount.setLutime(wenyAccount.getLutime());
        profitAccount.setPerson(person);
        profitAccount.setPersonName(personName);
        profitAccount.setState(0);
        profitAccount.setBankid(bankid);
        profitAccountMapper.insert(profitAccount);

    }

    @CjTransaction
    @Override
    public boolean hasWenyBankAccount(String person, String bankid) {
        WenyAccountExample example = new WenyAccountExample();
        example.createCriteria().andBankidEqualTo(bankid).andPersonEqualTo(person);
        return wenyAccountMapper.countByExample(example) > 0;
    }

    @CjTransaction
    @Override
    public Map<String, Object> createWallet(String person, String personName) {
        Map<String, Object> map = new HashMap<>();
        map.put("person", person);
        map.put("nickName", personName);
        if (hasWallet(person)) {
            map.put("code", "1201");
            map.put("message", "已初始化过");

            String p = person;
            RootAccount rootAccount = getRootAccount(p);
            map.put("rootAccount", rootAccount.getId());

            BalanceAccount balanceAccount = getBalanceAccount(p);
            map.put("balanceAccount", balanceAccount);

            AbsorbAccount absorbAccount = getAbsorbAccount(p);
            map.put("absorbAccount", absorbAccount);

            List<FreezenAccount> freezenAccounts = listFreezenAccount(p);
            map.put("freezenAccounts", freezenAccounts);

            List<ProfitAccount> profitAccounts = listProfitAccount(p);
            map.put("profitAccounts", profitAccounts);

            List<WenyAccount> wenyAccounts = listWenyAccount(p);
            map.put("wenyAccounts", wenyAccounts);

            return map;
        }
        map.put("code", "1200");
        map.put("message", "成功初始化");
        RootAccount rootAccount = new RootAccount();
        rootAccount.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        rootAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        rootAccount.setIsRealName(1);
        rootAccount.setLutime(rootAccount.getCtime());
        rootAccount.setPerson(person);
        rootAccount.setPersonName(personName);
        rootAccount.setState(0);
        rootAccount.setProperty(0);
        rootAccount.setOnorderAmount(0L);
        rootAccountMapper.insert(rootAccount);
        map.put("rootAccount", rootAccount.getId());

        BalanceAccount balanceAccount = new BalanceAccount();
        balanceAccount.setAmount(0L);
        balanceAccount.setCtime(rootAccount.getCtime());
        balanceAccount.setCurrency("CNY");
        balanceAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        balanceAccount.setLutime(balanceAccount.getCtime());
        balanceAccount.setPerson(person);
        balanceAccount.setPersonName(personName);
        balanceAccount.setState(0);
        balanceAccountMapper.insert(balanceAccount);
        map.put("balanceAccount", balanceAccount.getId());

        AbsorbAccount absorbAccount = new AbsorbAccount();
        absorbAccount.setAmount(new BigDecimal("0.00"));
        absorbAccount.setCtime(rootAccount.getCtime());
        absorbAccount.setCurrency("CNY");
        absorbAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        absorbAccount.setLutime(rootAccount.getLutime());
        absorbAccount.setPerson(person);
        absorbAccount.setPersonName(personName);
        absorbAccount.setState(0);
        absorbAccountMapper.insert(absorbAccount);
        map.put("absorbAccount", absorbAccount.getId());

        return map;
    }

    @CjTransaction
    @Override
    public Map<String, Object> getAllAccount(String person) {
        Map<String, Object> map = new HashMap<>();

        BalanceAccount balanceAccount = getBalanceAccount(person);
        if (balanceAccount != null) {
            map.put("balanceAccount", balanceAccount);
        }
        AbsorbAccount absorbAccount = getAbsorbAccount(person);
        if (absorbAccount != null) {
            map.put("absorbAccount", absorbAccount);
        }

        List<FreezenAccount> freezenAccounts = listFreezenAccount(person);
        if (freezenAccounts != null) {
            map.put("freezenAccounts", freezenAccounts);
        }

        List<ProfitAccount> profitAccounts = listProfitAccount(person);
        if (profitAccounts != null) {
            map.put("profitAccounts", profitAccounts);
        }

        List<WenyAccount> wenyAccounts = listWenyAccount(person);
        if (wenyAccounts != null) {
            map.put("wenyAccounts", wenyAccounts);
        }

        return map;
    }

    @CjTransaction
    @Override
    public Map<String, Object> getWenyAccounts(String person) {
        Map<String, Object> map = new HashMap<>();

        List<FreezenAccount> freezenAccounts = listFreezenAccount(person);
        if (freezenAccounts != null) {
            map.put("freezenAccounts", freezenAccounts);
        }

        List<ProfitAccount> profitAccounts = listProfitAccount(person);
        if (profitAccounts != null) {
            map.put("profitAccounts", profitAccounts);
        }

        List<WenyAccount> wenyAccounts = listWenyAccount(person);
        if (wenyAccounts != null) {
            map.put("wenyAccounts", wenyAccounts);
        }

        return map;
    }

    @CjTransaction
    @Override
    public AbsorbAccount getAbsorbAccount(String person) {
        AbsorbAccountExample absorbAccountExample = new AbsorbAccountExample();
        absorbAccountExample.createCriteria().andPersonEqualTo(person);
        List<AbsorbAccount> absorbAccountList = absorbAccountMapper.selectByExample(absorbAccountExample);
        if (absorbAccountList.isEmpty()) {
            return null;
        }
        return absorbAccountList.get(0);
    }

    @CjTransaction
    @Override
    public List<WenyAccount> listWenyAccount(String person) {
        WenyAccountExample wenyAccountExample = new WenyAccountExample();
        wenyAccountExample.createCriteria().andPersonEqualTo(person);
        List<WenyAccount> wenyAccountList = wenyAccountMapper.selectByExample(wenyAccountExample);
        return wenyAccountList;
    }

    @CjTransaction
    @Override
    public WenyAccount getWenyAccount(String person, String bankid) {
        WenyAccountExample wenyAccountExample = new WenyAccountExample();
        wenyAccountExample.createCriteria().andPersonEqualTo(person).andBankidEqualTo(bankid);
        List<WenyAccount> wenyAccountList = wenyAccountMapper.selectByExample(wenyAccountExample);
        return wenyAccountList.isEmpty() ? null : wenyAccountList.get(0);
    }

    @CjTransaction
    @Override
    public List<ProfitAccount> listProfitAccount(String person) {
        ProfitAccountExample profitAccountExample = new ProfitAccountExample();
        profitAccountExample.createCriteria().andPersonEqualTo(person);
        List<ProfitAccount> profitAccountList = profitAccountMapper.selectByExample(profitAccountExample);
        return profitAccountList;
    }

    @CjTransaction
    @Override
    public ProfitAccount getProfitAccount(String person, String bankid) {
        ProfitAccountExample profitAccountExample = new ProfitAccountExample();
        profitAccountExample.createCriteria().andPersonEqualTo(person).andBankidEqualTo(bankid);
        List<ProfitAccount> profitAccountList = profitAccountMapper.selectByExample(profitAccountExample);
        return profitAccountList.isEmpty() ? null : profitAccountList.get(0);
    }

    @CjTransaction
    @Override
    public List<FreezenAccount> listFreezenAccount(String person) {
        FreezenAccountExample freezenAccountExample = new FreezenAccountExample();
        freezenAccountExample.createCriteria().andPersonEqualTo(person);
        List<FreezenAccount> freezenAccountList = freezenAccountMapper.selectByExample(freezenAccountExample);
        return freezenAccountList;
    }

    @CjTransaction
    @Override
    public FreezenAccount getFreezenAccount(String person, String bankid) {
        FreezenAccountExample freezenAccountExample = new FreezenAccountExample();
        freezenAccountExample.createCriteria().andPersonEqualTo(person).andBankidEqualTo(bankid);
        List<FreezenAccount> freezenAccountList = freezenAccountMapper.selectByExample(freezenAccountExample);
        return freezenAccountList.isEmpty() ? null : freezenAccountList.get(0);
    }

    @CjTransaction
    @Override
    public BalanceAccount getBalanceAccount(String person) {
        BalanceAccountExample balanceAccountExample = new BalanceAccountExample();
        balanceAccountExample.createCriteria().andPersonEqualTo(person);
        List<BalanceAccount> balanceAccountList = balanceAccountMapper.selectByExample(balanceAccountExample);
        if (balanceAccountList.isEmpty()) {
            return null;
        }
        return balanceAccountList.get(0);
    }

    @CjTransaction
    @Override
    public RootAccount getRootAccount(String person) {
        RootAccountExample rootAccountExample = new RootAccountExample();
        rootAccountExample.createCriteria().andPersonEqualTo(person);
        List<RootAccount> rootAccountList = rootAccountMapper.selectByExample(rootAccountExample);
        if (rootAccountList.isEmpty()) {
            return null;
        }
        return rootAccountList.get(0);
    }

    @CjTransaction
    @Override
    public boolean hasWallet(String person) {
        RootAccountExample example = new RootAccountExample();
        example.createCriteria().andPersonEqualTo(person);
        return rootAccountMapper.countByExample(example) > 0;
    }

    @CjTransaction
    @Override
    public boolean hasFeeAccount(String payAccount) {
        FeeAccountExample example = new FeeAccountExample();
        example.createCriteria().andChannelAccountEqualTo(payAccount);
        return feeAccountMapper.countByExample(example) > 0;
    }

    @CjTransaction
    @Override
    public void createFeeAccount(String payChannel, String payAccount) {
        FeeAccount feeAccount = new FeeAccount();
        feeAccount.setAmount(0L);
        feeAccount.setCtime(WalletUtils.dateTimeToMicroSecond(System.currentTimeMillis()));
        feeAccount.setLutime(feeAccount.getCtime());
        feeAccount.setCurrency("CNY");
        feeAccount.setId(new IdWorker().nextId());
        feeAccount.setState(0);
        feeAccount.setPayChannel(payChannel);
        feeAccount.setChannelAccount(payAccount);
        feeAccountMapper.insert(feeAccount);
    }

    @CjTransaction
    @Override
    public FeeAccount getFeeAccount(String payAccount) {
        FeeAccountExample example = new FeeAccountExample();
        example.createCriteria().andChannelAccountEqualTo(payAccount);
        List<FeeAccount> list = feeAccountMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
