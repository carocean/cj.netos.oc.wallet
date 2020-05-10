package cj.netos.oc.wallet.model;

/**
 * Table: root_account
 */
public class RootAccount {
    /**
     * Column: id
     * Remark: uuid
     */
    private String id;

    /**
     * Column: person
     * Remark: 公众号
     */
    private String person;

    /**
     * Column: phone
     * Remark: 已验证的手机号
     */
    private String phone;

    /**
     * Column: person_name
     */
    private String personName;

    /**
     * Column: is_real_name
     * Remark: 是否实名，0为未认证，1为已认证
     */
    private Integer isRealName;

    /**
     * Column: property
     * Remark: 账户性质 1-个人 2-企业
     */
    private Integer property;

    /**
     * Column: state
     * Remark: 00 待充值 01 充值中 02 成功 03 失败 
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
     * Column: sign_value
     * Remark: 签名	免密协议签名
     */
    private String signValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person == null ? null : person.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName == null ? null : personName.trim();
    }

    public Integer getIsRealName() {
        return isRealName;
    }

    public void setIsRealName(Integer isRealName) {
        this.isRealName = isRealName;
    }

    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
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

    public String getSignValue() {
        return signValue;
    }

    public void setSignValue(String signValue) {
        this.signValue = signValue == null ? null : signValue.trim();
    }
}