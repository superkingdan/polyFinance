package com.jnshu.entity;

public class TransactionLogRPO {
    private long id;
    private int page=1;
    private int size=10;
    private String productName;
    private int transactionStyle;
    private long transactionAtMin;
    private long transactionAtMax;
    private int transactionStatus;

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

    public int getTransactionStyle() {
        return transactionStyle;
    }

    public void setTransactionStyle(int transactionStyle) {
        this.transactionStyle = transactionStyle;
    }

    public long getTransactionAtMin() {
        return transactionAtMin;
    }

    public void setTransactionAtMin(long transactionAtMin) {
        this.transactionAtMin = transactionAtMin;
    }

    public long getTransactionAtMax() {
        return transactionAtMax;
    }

    public void setTransactionAtMax(long transactionAtMax) {
        this.transactionAtMax = transactionAtMax;
    }

    public int getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(int transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public String toString() {
        return "TransactionLogRPO{" +
                "id='" + id + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", productName='" + productName + '\'' +
                ", transactionStyle=" + transactionStyle +
                ", transactionAtMin=" + transactionAtMin +
                ", transactionAtMax=" + transactionAtMax +
                ", transactionStatus=" + transactionStatus +
                '}';
    }
}
