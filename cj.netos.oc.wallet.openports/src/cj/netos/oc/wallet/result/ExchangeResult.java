package cj.netos.oc.wallet.result;

import cj.netos.oc.wallet.bo.ExchangedBO;

public class ExchangeResult {
    String walletPuchaseSn;
    String bankExchangeSn;
    String bankPurchaseSn;
    String bankid;
    String sn;

    public void load(ExchangedBO bo) {
        walletPuchaseSn = bo.getWalletPurchaseSn();
        bankExchangeSn = bo.getBankExchangeSn();
        bankPurchaseSn = bo.getBankPurchaseSn();
        bankid = bo.getBankid();
        sn = bo.getSn();
    }

    public String getWalletPuchaseSn() {
        return walletPuchaseSn;
    }

    public void setWalletPuchaseSn(String walletPuchaseSn) {
        this.walletPuchaseSn = walletPuchaseSn;
    }

    public String getBankExchangeSn() {
        return bankExchangeSn;
    }

    public void setBankExchangeSn(String bankExchangeSn) {
        this.bankExchangeSn = bankExchangeSn;
    }

    public String getBankPurchaseSn() {
        return bankPurchaseSn;
    }

    public void setBankPurchaseSn(String bankPurchaseSn) {
        this.bankPurchaseSn = bankPurchaseSn;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
