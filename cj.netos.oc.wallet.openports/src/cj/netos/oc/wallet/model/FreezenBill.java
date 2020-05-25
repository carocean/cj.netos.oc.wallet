package cj.netos.oc.wallet.model;

/**
 * Table: freezen_bill
 */
public class FreezenBill {
    /**
     * Column: sn
     * Remark: 流水号	YYYYMMDD+帐务流水（每天从1开始）
     */
    private String sn;

    /**
     * Column: accountid
     * Remark: 属于哪个账户id
     */
    private String accountid;

    /**
     * Column: title
     * Remark: 显示名
     */
    private String title;

    /**
     * Column: order
     * Remark: 01 充值  02 提现 03 转账 04 支付 05 收款 06原交易退款 07原交易撤销 08申购纹银 09承况纹银
     */
    private Integer order;

    /**
     * Column: amount
     * Remark: 发生金额
     */
    private Long amount;

    /**
     * Column: balance
     * Remark: 变动后余额
     */
    private Long balance;

    /**
     * Column: refsn
     * Remark: 关联流水ID	Order对应不同的表，充值流水 	支付流水 	提现流水
     */
    private String refsn;

    /**
     * Column: ctime
     * Remark: 创建时间
     */
    private String ctime;

    /**
     * Column: note
     * Remark: 备注
     */
    private String note;

    /**
     * Column: workday
     * Remark: 会计日期
     */
    private String workday;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid == null ? null : accountid.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getRefsn() {
        return refsn;
    }

    public void setRefsn(String refsn) {
        this.refsn = refsn == null ? null : refsn.trim();
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime == null ? null : ctime.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday == null ? null : workday.trim();
    }
}