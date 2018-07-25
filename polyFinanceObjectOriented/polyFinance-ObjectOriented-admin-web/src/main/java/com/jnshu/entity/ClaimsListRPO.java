package com.jnshu.entity;

/**
 * 获得债权列表前端传参接收类
 */
public class ClaimsListRPO {
    private int page=1;
    private int size=10;
    private String creditor;
    private String creditorIdCard;
    private long lendStartMin;
    private long lendStartMax;
    private String ClaimsCode;
    private int status;
    private long lendEndMin;
    private long lendEndMax;
    private String creditorPhoneNumber;
    private String lendMoneyMin;
    private String lendMoneyMax;
    //单位：月
    private long lendDeadlineMin;
    private long lendDeadlineMax;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public String getCreditorIdCard() {
        return creditorIdCard;
    }

    public void setCreditorIdCard(String creditorIdCard) {
        this.creditorIdCard = creditorIdCard;
    }

    public long getLendStartMin() {
        return lendStartMin;
    }

    public void setLendStartMin(long lendStartMin) {
        this.lendStartMin = lendStartMin;
    }

    public long getLendStartMax() {
        return lendStartMax;
    }

    public void setLendStartMax(long lendStartMax) {
        this.lendStartMax = lendStartMax;
    }

    public String getClaimsCode() {
        return ClaimsCode;
    }

    public void setClaimsCode(String claimsCode) {
        ClaimsCode = claimsCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getLendEndMin() {
        return lendEndMin;
    }

    public void setLendEndMin(long lendEndMin) {
        this.lendEndMin = lendEndMin;
    }

    public long getLendEndMax() {
        return lendEndMax;
    }

    public void setLendEndMax(long lendEndMax) {
        this.lendEndMax = lendEndMax;
    }

    public String getCreditorPhoneNumber() {
        return creditorPhoneNumber;
    }

    public void setCreditorPhoneNumber(String creditorPhoneNumber) {
        this.creditorPhoneNumber = creditorPhoneNumber;
    }

    public String getLendMoneyMin() {
        return lendMoneyMin;
    }

    public void setLendMoneyMin(String lendMoneyMin) {
        this.lendMoneyMin = lendMoneyMin;
    }

    public String getLendMoneyMax() {
        return lendMoneyMax;
    }

    public void setLendMoneyMax(String lendMoneyMax) {
        this.lendMoneyMax = lendMoneyMax;
    }

    public long getLendDeadlineMin() {
        return lendDeadlineMin;
    }

    public void setLendDeadlineMin(long lendDeadlineMin) {
        this.lendDeadlineMin = lendDeadlineMin;
    }

    public long getLendDeadlineMax() {
        return lendDeadlineMax;
    }

    public void setLendDeadlineMax(long lendDeadlineMax) {
        this.lendDeadlineMax = lendDeadlineMax;
    }

    @Override
    public String toString() {
        return "ClaimsListRPO{" +
                "page=" + page +
                ", size=" + size +
                ", creditor='" + creditor + '\'' +
                ", creditorIdCard='" + creditorIdCard + '\'' +
                ", lendStartMin=" + lendStartMin +
                ", lendStartMax=" + lendStartMax +
                ", ClaimsCode='" + ClaimsCode + '\'' +
                ", status=" + status +
                ", lendEndMin=" + lendEndMin +
                ", lendEndMax=" + lendEndMax +
                ", creditorPhoneNumber='" + creditorPhoneNumber + '\'' +
                ", lendMoneyMin='" + lendMoneyMin + '\'' +
                ", lendMoneyMax='" + lendMoneyMax + '\'' +
                ", lendDeadlineMin=" + lendDeadlineMin +
                ", lendDeadlineMax=" + lendDeadlineMax +
                '}';
    }
}
