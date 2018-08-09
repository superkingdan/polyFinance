package com.jnshu.entity;

import java.io.Serializable;

/**
 * 聚金融claims表对应实体类
 */
public class Claims implements Serializable {
    private static final long serialVersionUID = -931727715216503734L;
    /**
     * 状态，未使用
     */
    public static final Integer STATUS_NOT_USE=0;
    /**
     * 状态，使用中
     */
    public static final Integer STATUS_USING=1;
    /**
     * 状态，已到期
     */
    public static final Integer STATUS_EXPIRED=2;
    private Long id;
    private Long createAt;
    private Long createBy;
    private Long updateAt;
    private Long updateBy;
    //债权代号
    private String claimsCode;
    private String creditor;
    private String creditorPhoneNumber;
    private String creditorIdCard;
    //出借期限，单位：月
    private Integer lendDeadline;
    private Long lendStartAt;
    private Long lendEndAt;
    private String lendMoney;
    //债权状态，0:未使用，1：使用中，2：已到期，默认为0
    private Integer status=0;
    private String claimsNature;
    private String claimsInterestRate;
    private String remark;
    private String remanentMoney;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getClaimsCode() {
        return claimsCode;
    }

    public void setClaimsCode(String claimsCode) {
        this.claimsCode = claimsCode;
    }

    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public String getCreditorPhoneNumber() {
        return creditorPhoneNumber;
    }

    public void setCreditorPhoneNumber(String creditorPhoneNumber) {
        this.creditorPhoneNumber = creditorPhoneNumber;
    }

    public String getCreditorIdCard() {
        return creditorIdCard;
    }

    public void setCreditorIdCard(String creditorIdCard) {
        this.creditorIdCard = creditorIdCard;
    }

    public Integer getLendDeadline() {
        return lendDeadline;
    }

    public void setLendDeadline(Integer lendDeadline) {
        this.lendDeadline = lendDeadline;
    }

    public Long getLendStartAt() {
        return lendStartAt;
    }

    public void setLendStartAt(Long lendStartAt) {
        this.lendStartAt = lendStartAt;
    }

    public Long getLendEndAt() {
        return lendEndAt;
    }

    public void setLendEndAt(Long lendEndAt) {
        this.lendEndAt = lendEndAt;
    }

    public String getLendMoney() {
        return lendMoney;
    }

    public void setLendMoney(String lendMoney) {
        this.lendMoney = lendMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClaimsNature() {
        return claimsNature;
    }

    public void setClaimsNature(String claimsNature) {
        this.claimsNature = claimsNature;
    }

    public String getClaimsInterestRate() {
        return claimsInterestRate;
    }

    public void setClaimsInterestRate(String claimsInterestRate) {
        this.claimsInterestRate = claimsInterestRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemanentMoney() {
        return remanentMoney;
    }

    public void setRemanentMoney(String remanentMoney) {
        this.remanentMoney = remanentMoney;
    }

    @Override
    public String toString() {
        return "Claims{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", createBy=" + createBy +
                ", updateAt=" + updateAt +
                ", updateBy=" + updateBy +
                ", claimsCode='" + claimsCode + '\'' +
                ", creditor='" + creditor + '\'' +
                ", creditorPhoneNumber='" + creditorPhoneNumber + '\'' +
                ", creditorIdCard='" + creditorIdCard + '\'' +
                ", lendDeadline=" + lendDeadline +
                ", lendStartAt=" + lendStartAt +
                ", lendEndAt=" + lendEndAt +
                ", lendMoney='" + lendMoney + '\'' +
                ", status=" + status +
                ", claimsNature='" + claimsNature + '\'' +
                ", claimsInterestRate='" + claimsInterestRate + '\'' +
                ", remark='" + remark + '\'' +
                ", remanentMoney='" + remanentMoney + '\'' +
                '}';
    }
}
