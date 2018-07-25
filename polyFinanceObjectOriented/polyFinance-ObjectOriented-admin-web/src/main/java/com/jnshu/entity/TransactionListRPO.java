package com.jnshu.entity;

public class TransactionListRPO {
    private long id;
    private int page=1;
    private int size=10;
    private String productName;
    private String contractCode;
    private long startAtMin;
    private long startAtMax;
    private String claimsProtocolCode;
    private int status;
    private long endAtMin;
    private long endAtMax;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public long getStartAtMin() {
        return startAtMin;
    }

    public void setStartAtMin(long startAtMin) {
        this.startAtMin = startAtMin;
    }

    public long getStartAtMax() {
        return startAtMax;
    }

    public void setStartAtMax(long startAtMax) {
        this.startAtMax = startAtMax;
    }

    public String getClaimsProtocolCode() {
        return claimsProtocolCode;
    }

    public void setClaimsProtocolCode(String claimsProtocolCode) {
        this.claimsProtocolCode = claimsProtocolCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getEndAtMin() {
        return endAtMin;
    }

    public void setEndAtMin(long endAtMin) {
        this.endAtMin = endAtMin;
    }

    public long getEndAtMax() {
        return endAtMax;
    }

    public void setEndAtMax(long endAtMax) {
        this.endAtMax = endAtMax;
    }

    @Override
    public String toString() {
        return "TransactionListRPO{" +
                "id=" + id +
                ", page=" + page +
                ", size=" + size +
                ", productName='" + productName + '\'' +
                ", contractCode='" + contractCode + '\'' +
                ", startAtMin=" + startAtMin +
                ", startAtMax=" + startAtMax +
                ", claimsProtocolCode='" + claimsProtocolCode + '\'' +
                ", status=" + status +
                ", endAtMin=" + endAtMin +
                ", endAtMax=" + endAtMax +
                '}';
    }
}
