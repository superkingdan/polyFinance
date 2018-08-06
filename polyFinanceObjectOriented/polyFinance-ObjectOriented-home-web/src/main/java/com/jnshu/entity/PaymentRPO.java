package com.jnshu.entity;

/**
 * 确认支付用户支付信息
 */
public class PaymentRPO {
    private long userId;
    private long bankCardId;
    private String money;
    private long productId;
    private long contractId;
    private String userSign;

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(long bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    @Override
    public String toString() {
        return "PaymentRPO{" +
                "userId=" + userId +
                ", bankCardId=" + bankCardId +
                ", money='" + money + '\'' +
                ", productId=" + productId +
                ", contractId=" + contractId +
                ", userSign='" + userSign + '\'' +
                '}';
    }
}
