package cj.netos.oc.wallet.model;

import java.math.BigDecimal;

/**
 * Table: work_switch_day
 */
public class WorkSwitchDay {
    /**
     * Column: id
     */
    private String id;

    /**
     * Column: work_day
     */
    private String workDay;

    /**
     * Column: pre_amount
     * Remark: 上期余额账户余额
     */
    private Long preAmount;

    /**
     * Column: unckeck_amount
     */
    private Long unckeckAmount;

    /**
     * Column: checked_amount
     */
    private Long checkedAmount;

    /**
     * Column: pre_freezen_amount
     */
    private Long preFreezenAmount;

    /**
     * Column: unckeck_freezen_amount
     */
    private Long unckeckFreezenAmount;

    /**
     * Column: checked_freezen_amount
     */
    private Long checkedFreezenAmount;

    /**
     * Column: pre_profit_amount
     */
    private Long preProfitAmount;

    /**
     * Column: unckech_profit_amount
     */
    private Long unckechProfitAmount;

    /**
     * Column: checked_profit_amount
     */
    private Long checkedProfitAmount;

    /**
     * Column: pre_yinj_amount
     */
    private Long preYinjAmount;

    /**
     * Column: uncheck_yinj_amount
     */
    private Long uncheckYinjAmount;

    /**
     * Column: checked_yinj_amount
     */
    private Long checkedYinjAmount;

    /**
     * Column: pre_weny_quantities
     */
    private BigDecimal preWenyQuantities;

    /**
     * Column: unckeck_weny_quatities
     */
    private BigDecimal unckeckWenyQuatities;

    /**
     * Column: checked_weny_quantities
     */
    private BigDecimal checkedWenyQuantities;

    /**
     * Column: day_switch_state
     * Remark: 日切状态: 0 正在日切 1 日切完成
     */
    private Integer daySwitchState;

    /**
     * Column: note
     * Remark: 审计备注
     */
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay == null ? null : workDay.trim();
    }

    public Long getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(Long preAmount) {
        this.preAmount = preAmount;
    }

    public Long getUnckeckAmount() {
        return unckeckAmount;
    }

    public void setUnckeckAmount(Long unckeckAmount) {
        this.unckeckAmount = unckeckAmount;
    }

    public Long getCheckedAmount() {
        return checkedAmount;
    }

    public void setCheckedAmount(Long checkedAmount) {
        this.checkedAmount = checkedAmount;
    }

    public Long getPreFreezenAmount() {
        return preFreezenAmount;
    }

    public void setPreFreezenAmount(Long preFreezenAmount) {
        this.preFreezenAmount = preFreezenAmount;
    }

    public Long getUnckeckFreezenAmount() {
        return unckeckFreezenAmount;
    }

    public void setUnckeckFreezenAmount(Long unckeckFreezenAmount) {
        this.unckeckFreezenAmount = unckeckFreezenAmount;
    }

    public Long getCheckedFreezenAmount() {
        return checkedFreezenAmount;
    }

    public void setCheckedFreezenAmount(Long checkedFreezenAmount) {
        this.checkedFreezenAmount = checkedFreezenAmount;
    }

    public Long getPreProfitAmount() {
        return preProfitAmount;
    }

    public void setPreProfitAmount(Long preProfitAmount) {
        this.preProfitAmount = preProfitAmount;
    }

    public Long getUnckechProfitAmount() {
        return unckechProfitAmount;
    }

    public void setUnckechProfitAmount(Long unckechProfitAmount) {
        this.unckechProfitAmount = unckechProfitAmount;
    }

    public Long getCheckedProfitAmount() {
        return checkedProfitAmount;
    }

    public void setCheckedProfitAmount(Long checkedProfitAmount) {
        this.checkedProfitAmount = checkedProfitAmount;
    }

    public Long getPreYinjAmount() {
        return preYinjAmount;
    }

    public void setPreYinjAmount(Long preYinjAmount) {
        this.preYinjAmount = preYinjAmount;
    }

    public Long getUncheckYinjAmount() {
        return uncheckYinjAmount;
    }

    public void setUncheckYinjAmount(Long uncheckYinjAmount) {
        this.uncheckYinjAmount = uncheckYinjAmount;
    }

    public Long getCheckedYinjAmount() {
        return checkedYinjAmount;
    }

    public void setCheckedYinjAmount(Long checkedYinjAmount) {
        this.checkedYinjAmount = checkedYinjAmount;
    }

    public BigDecimal getPreWenyQuantities() {
        return preWenyQuantities;
    }

    public void setPreWenyQuantities(BigDecimal preWenyQuantities) {
        this.preWenyQuantities = preWenyQuantities;
    }

    public BigDecimal getUnckeckWenyQuatities() {
        return unckeckWenyQuatities;
    }

    public void setUnckeckWenyQuatities(BigDecimal unckeckWenyQuatities) {
        this.unckeckWenyQuatities = unckeckWenyQuatities;
    }

    public BigDecimal getCheckedWenyQuantities() {
        return checkedWenyQuantities;
    }

    public void setCheckedWenyQuantities(BigDecimal checkedWenyQuantities) {
        this.checkedWenyQuantities = checkedWenyQuantities;
    }

    public Integer getDaySwitchState() {
        return daySwitchState;
    }

    public void setDaySwitchState(Integer daySwitchState) {
        this.daySwitchState = daySwitchState;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }
}