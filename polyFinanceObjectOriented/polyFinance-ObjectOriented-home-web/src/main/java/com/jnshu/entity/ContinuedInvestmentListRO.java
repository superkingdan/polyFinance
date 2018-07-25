package com.jnshu.entity;

/**
 * 续投列表对象
 */
public class ContinuedInvestmentListRO {
    private long id;
    private String productName;
    private String interestRate;
    private String money;
    private int mark;
    private long startAt;
    private long endAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
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

    @Override
    public String toString() {
        return "ContinuedInvestmentListRO{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", interestRate='" + interestRate + '\'' +
                ", money='" + money + '\'' +
                ", mark=" + mark +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                '}';
    }
}
