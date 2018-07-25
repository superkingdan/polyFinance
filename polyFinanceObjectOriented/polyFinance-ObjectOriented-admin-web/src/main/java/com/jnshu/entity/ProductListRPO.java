package com.jnshu.entity;

/**
 * 获得产品列表分页接收数据实体类
 */
public class ProductListRPO {
    private int page=1;
    private int size=10;
    private int status;
    private String productName;
    private int rateOfInterest;
    private String interestRateMin;
    private String interestRateMax;
    private String investmentAmount;
    private int deadlineMin;
    private int deadlineMax;
    private String productCode;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(int rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public String getInterestRateMin() {
        return interestRateMin;
    }

    public void setInterestRateMin(String interestRateMin) {
        this.interestRateMin = interestRateMin;
    }

    public String getInterestRateMax() {
        return interestRateMax;
    }

    public void setInterestRateMax(String interestRateMax) {
        this.interestRateMax = interestRateMax;
    }

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(String investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public int getDeadlineMin() {
        return deadlineMin;
    }

    public void setDeadlineMin(int deadlineMin) {
        this.deadlineMin = deadlineMin;
    }

    public int getDeadlineMax() {
        return deadlineMax;
    }

    public void setDeadlineMax(int deadlineMax) {
        this.deadlineMax = deadlineMax;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "ProductListRPO{" +
                "page=" + page +
                ", size=" + size +
                ", status=" + status +
                ", productName='" + productName + '\'' +
                ", rateOfInterest=" + rateOfInterest +
                ", interestRateMin='" + interestRateMin + '\'' +
                ", interestRateMax='" + interestRateMax + '\'' +
                ", investmentAmount='" + investmentAmount + '\'' +
                ", deadlineMin=" + deadlineMin +
                ", deadlineMax=" + deadlineMax +
                ", productCode='" + productCode + '\'' +
                '}';
    }
}
