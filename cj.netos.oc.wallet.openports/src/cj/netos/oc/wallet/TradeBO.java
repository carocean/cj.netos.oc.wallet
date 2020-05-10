package cj.netos.oc.wallet;

/**
 * 交易流水表
 */
public class TradeBO {
    String id;
    long amount;
    String order;
    String state;
    long balance;
    long ctime;
    TradeDetailsBO details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public TradeDetailsBO getDetails() {
        return details;
    }

    public void setDetails(TradeDetailsBO details) {
        this.details = details;
    }
}
