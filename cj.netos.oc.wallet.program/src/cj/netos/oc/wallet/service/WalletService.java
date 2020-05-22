package cj.netos.oc.wallet.service;

import cj.netos.oc.wallet.IWalletService;
import cj.netos.oc.wallet.Person;
import cj.netos.oc.wallet.mapper.*;
import cj.netos.oc.wallet.model.*;
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

            FreezenAccount freezenAccount = getFreezenAccount(p);
            map.put("freezenAccount", freezenAccount);

            ProfitAccount profitAccount = getProfitAccount(p);
            map.put("profitAccount", profitAccount);

            AbsorbAccount absorbAccount = getAbsorbAccount(p);
            map.put("absorbAccount", absorbAccount);

            WenyAccount wenyAccount = getWenyAccount(p);
            map.put("wenyAccount", wenyAccount);

            return map;
        }
        map.put("code", "1200");
        map.put("message", "成功初始化");
        RootAccount rootAccount = new RootAccount();
        rootAccount.setCtime(WalletUtils.dateTimeToSecond(System.currentTimeMillis()));
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

        FreezenAccount freezenAccount = new FreezenAccount();
        freezenAccount.setAmount(0L);
        freezenAccount.setCtime(rootAccount.getCtime());
        freezenAccount.setCurrency("CNY");
        freezenAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        freezenAccount.setLutime(rootAccount.getLutime());
        freezenAccount.setPerson(person);
        freezenAccount.setPersonName(personName);
        freezenAccount.setState(0);
        freezenAccountMapper.insert(freezenAccount);
        map.put("freezenAccount", freezenAccount.getId());

        ProfitAccount profitAccount = new ProfitAccount();
        profitAccount.setAmount(0L);
        profitAccount.setCtime(rootAccount.getCtime());
        profitAccount.setCurrency("CNY");
        profitAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        profitAccount.setLutime(rootAccount.getLutime());
        profitAccount.setPerson(person);
        profitAccount.setPersonName(personName);
        profitAccount.setState(0);
        profitAccountMapper.insert(profitAccount);
        map.put("profitAccount", profitAccount.getId());

        AbsorbAccount absorbAccount = new AbsorbAccount();
        absorbAccount.setAmount(0L);
        absorbAccount.setCtime(rootAccount.getCtime());
        absorbAccount.setCurrency("CNY");
        absorbAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        absorbAccount.setLutime(rootAccount.getLutime());
        absorbAccount.setPerson(person);
        absorbAccount.setPersonName(personName);
        absorbAccount.setState(0);
        absorbAccountMapper.insert(absorbAccount);
        map.put("absorbAccount", absorbAccount.getId());

        WenyAccount wenyAccount = new WenyAccount();
        wenyAccount.setQuanlities(BigDecimal.ZERO);
        wenyAccount.setCtime(rootAccount.getCtime());
        wenyAccount.setCurrency("WENY");
        wenyAccount.setId(Encript.md5(System.currentTimeMillis() + UUID.randomUUID().toString()));
        wenyAccount.setLutime(rootAccount.getLutime());
        wenyAccount.setPerson(person);
        wenyAccount.setPersonName(personName);
        wenyAccount.setState(0);
        wenyAccountMapper.insert(wenyAccount);
        map.put("wenyAccount", wenyAccount.getId());

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

        FreezenAccount freezenAccount = getFreezenAccount(person);
        if (freezenAccount != null) {
            map.put("freezenAccount", freezenAccount);
        }

        ProfitAccount profitAccount = getProfitAccount(person);
        if (profitAccount != null) {
            map.put("profitAccount", profitAccount);
        }

        AbsorbAccount absorbAccount = getAbsorbAccount(person);
        if (absorbAccount != null) {
            map.put("absorbAccount", absorbAccount);
        }

        WenyAccount wenyAccount = getWenyAccount(person);
        if (wenyAccount != null) {
            map.put("absorbAccount", wenyAccount);
        }

        return map;
    }

    @CjTransaction
    @Override
    public WenyAccount getWenyAccount(String person) {
        WenyAccountExample wenyAccountExample = new WenyAccountExample();
        wenyAccountExample.createCriteria().andPersonEqualTo(person);
        List<WenyAccount> wenyAccountList = wenyAccountMapper.selectByExample(wenyAccountExample);
        if (wenyAccountList.isEmpty()) {
            return null;
        }
        return wenyAccountList.get(0);
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
    public ProfitAccount getProfitAccount(String person) {
        ProfitAccountExample profitAccountExample = new ProfitAccountExample();
        profitAccountExample.createCriteria().andPersonEqualTo(person);
        List<ProfitAccount> profitAccountList = profitAccountMapper.selectByExample(profitAccountExample);
        if (profitAccountList.isEmpty()) {
            return null;
        }
        return profitAccountList.get(0);
    }

    @CjTransaction
    @Override
    public FreezenAccount getFreezenAccount(String person) {
        FreezenAccountExample freezenAccountExample = new FreezenAccountExample();
        freezenAccountExample.createCriteria().andPersonEqualTo(person);
        List<FreezenAccount> freezenAccountList = freezenAccountMapper.selectByExample(freezenAccountExample);
        if (freezenAccountList.isEmpty()) {
            return null;
        }
        return freezenAccountList.get(0);
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

}
