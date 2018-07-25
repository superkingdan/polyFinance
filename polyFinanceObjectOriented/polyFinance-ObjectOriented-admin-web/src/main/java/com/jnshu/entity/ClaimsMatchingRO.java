package com.jnshu.entity;

/**
 * 获得指定债权匹配列表data2数据实体类
 */
public class ClaimsMatchingRO {
    private String contractCode;
    private String claimsProtocolCode;
    private String userName;
    private String productName;
    private long startAt;
    private long endAt;
    private String money;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getClaimsProtocolCode() {
        return claimsProtocolCode;
    }

    public void setClaimsProtocolCode(String claimsProtocolCode) {
        this.claimsProtocolCode = claimsProtocolCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "ClaimsMatchingRO{" +
                "contractCode='" + contractCode + '\'' +
                ", claimsProtocolCode='" + claimsProtocolCode + '\'' +
                ", userName='" + userName + '\'' +
                ", productName='" + productName + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", money='" + money + '\'' +
                '}';
    }
}
