package com.jnshu.entity;

/**
 * 确认支付用户支付信息
 */
public class PaymentRPO {
    private Long userId;
    private Long bankCardId;
    private String money;
    private Long productId;
    private Long contractId;
    private String userSign;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Long bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
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
