package cj.netos.oc.wallet;

/**
 * 事务控制表
 */
public class TransactionBO {
    String id;
    long ctime;
    boolean isValid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
