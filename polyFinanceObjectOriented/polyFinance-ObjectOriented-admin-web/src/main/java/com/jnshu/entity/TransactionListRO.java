package com.jnshu.entity;

public class TransactionListRO {
    private String productName;
    private String money;
    private long startAt;
    private long endAt;
    private int status;
    private String returned;
    private String notReturn;
    private String contractCode;
    private String claimsProtocolCode;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReturned() {
        return returned;
    }

    public void setReturned(String returned) {
        this.returned = returned;
    }

    public String getNotReturn() {
        return notReturn;
    }

    public void setNotReturn(String notReturn) {
        this.notReturn = notReturn;
    }

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

    @Override
    public String toString() {
        return "TransactionListRO{" +
                "productName='" + productName + '\'' +
                ", money='" + money + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", status=" + status +
                ", returned='" + returned + '\'' +
                ", notReturn='" + notReturn + '\'' +
                ", contractCode='" + contractCode + '\'' +
                ", claimsProtocolCode='" + claimsProtocolCode + '\'' +
                '}';
    }
}
