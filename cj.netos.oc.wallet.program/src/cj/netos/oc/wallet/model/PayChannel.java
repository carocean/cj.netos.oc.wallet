package cj.netos.oc.wallet.model;

/**
 * Table: pay_channel
 */
public class PayChannel {
    /**
     * Column: id
     * Remark: 标识
     */
    private String id;

    /**
     * Column: type_code
     * Remark: 渠道类型代码， 0:微信 1:支付宝 2:银联
     */
    private String typeCode;

    /**
     * Column: url
     * Remark: 渠道的服务地址
     */
    private String url;

    /**
     * Column: appid
     * Remark: 渠道给的aped
     */
    private String appid;

    /**
     * Column: appSecret
     * Remark: 渠道给的密钥
     */
    private String appsecret;

    /**
     * Column: mch_id
     * Remark: 商户号
     */
    private String mchId;

    /**
     * Column: note
     * Remark: 备注
     */
    private String note;

    /**
     * Column: limit_amount
     * Remark: 渠道限额
     */
    private Long limitAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode == null ? null : typeCode.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret == null ? null : appsecret.trim();
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Long getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Long limitAmount) {
        this.limitAmount = limitAmount;
    }
}