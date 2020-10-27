package cj.netos.oc.wallet.bo;

import cj.ultimate.gson2.com.google.gson.Gson;

public class WithdrawBO {
    /**
     * Column: sn
     * Remark: YYYYMMDD+交易流水（每天从1开始）
     */
    private String sn;

    /**
     * Column: person
     * Remark: 公众号
     */
    private String person;

    /**
     * Column: person_name
     * Remark: 公众名
     */
    private String personName;

    /**
     * Column: currency
     * Remark: 币种
     */
    private String currency;

    /**
     * Column: demand_amount
     * Remark: 欲申购金
     */
    private Long demandAmount;

    /**
     * Column: real_amount
     * Remark: 实际渠道接收的金额
     */
    private Long realAmount;

    /**
     * Column: pay_channel
     * Remark: 出款渠道，即提现方式，为yongyu字段，与公众账户对应，如提现到个人支付宝，个人微信，银联
     */
    private String payChannel;

    /**
     * Column: pay_account
     * Remark: 付款的账户，可能为空，如支付宝付款者为个人账户是记录不到的；但在网联方式下可以记录下用户从哪个银行卡付的款，因此将来接网联还需要建立用户-银行卡绑定表
     */
    private String payAccount;

    /**
     * Column: person_card
     * Remark: 收款公众卡
     */
    private String personCard;


    /**
     * Column: state
     * Remark: -1提现失败 0-待充值 1-渠道已决清
     2- 提现成功
     */
    private Integer state;

    /**
     * Column: ctime
     * Remark: 创建时间
     */
    private String ctime;

    /**
     * Column: lutime
     * Remark: 最新修改时间
     */
    private String lutime;

    /**
     * Column: note
     */
    private String note;

    /**
     * Column: settle_code
     * Remark: 订单完成时第三方渠道的返回码
     */
    private String settleCode;

    /**
     * Column: settle_msg
     * Remark: 订单完成时第三方渠道的返回信息
     */
    private String settleMsg;


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person == null ? null : person.trim();
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName == null ? null : personName.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public Long getDemandAmount() {
        return demandAmount;
    }

    public void setDemandAmount(Long demandAmount) {
        this.demandAmount = demandAmount;
    }

    public Long getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Long realAmount) {
        this.realAmount = realAmount;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPersonCard() {
        return personCard;
    }

    public void setPersonCard(String personCard) {
        this.personCard = personCard;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime == null ? null : ctime.trim();
    }

    public String getLutime() {
        return lutime;
    }

    public void setLutime(String lutime) {
        this.lutime = lutime == null ? null : lutime.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getSettleCode() {
        return settleCode;
    }

    public void setSettleCode(String settleCode) {
        this.settleCode = settleCode == null ? null : settleCode.trim();
    }

    public String getSettleMsg() {
        return settleMsg;
    }

    public void setSettleMsg(String settleMsg) {
        this.settleMsg = settleMsg == null ? null : settleMsg.trim();
    }
}
