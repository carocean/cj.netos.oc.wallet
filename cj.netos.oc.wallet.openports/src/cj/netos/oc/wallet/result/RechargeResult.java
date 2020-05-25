package cj.netos.oc.wallet.result;

import cj.netos.oc.wallet.bo.RechargeBO;

public class RechargeResult {
    String sn;
    String person;
    String status;
    String message;
    String payChannel;
    Object record;

    public RechargeResult() {
    }

    public RechargeResult(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static RechargeResult create(RechargeBO bo) {
        RechargeResult rechargeResult = new RechargeResult();
        rechargeResult.setSn(bo.getSn());
        rechargeResult.setPayChannel(bo.getFromChannel());
        rechargeResult.setPerson(bo.getPerson());
        rechargeResult.setStatus(bo.getStatus()+"");
        rechargeResult.setMessage(bo.getMessage());
        rechargeResult.setRecord(bo);
        return rechargeResult;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRecord() {
        return record;
    }

    public void setRecord(Object record) {
        this.record = record;
    }
}
