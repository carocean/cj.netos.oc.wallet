package cj.netos.oc.wallet;

import java.math.BigDecimal;

/**
 * 余额表。纹银余额、现金余额
 */
public class BalanceBO {
    long fundBalance;
    BigDecimal wenyBalance;

    public long getFundBalance() {
        return fundBalance;
    }

    public void setFundBalance(long fundBalance) {
        this.fundBalance = fundBalance;
    }

    public BigDecimal getWenyBalance() {
        return wenyBalance;
    }

    public void setWenyBalance(BigDecimal wenyBalance) {
        this.wenyBalance = wenyBalance;
    }
}
