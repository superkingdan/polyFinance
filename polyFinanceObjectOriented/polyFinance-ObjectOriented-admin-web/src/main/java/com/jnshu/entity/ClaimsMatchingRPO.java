package com.jnshu.entity;

/**
 * 获得指定债权匹配列表接收传参实体类
 */
public class ClaimsMatchingRPO {
    private long id;
    private int page=1;
    private int size=10;
    private String productName;
    private String userName;
    private long startAtMin;
    private long startAtMax;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return "ClaimsMatchingRPO{" +
                "id=" + id +
                ", page=" + page +
                ", size=" + size +
                ", productName='" + productName + '\'' +
                ", userName='" + userName + '\'' +
                ", startAtMin=" + startAtMin +
                ", startAtMax=" + startAtMax +
                ", endAtMin=" + endAtMin +
                ", endAtMax=" + endAtMax +
                '}';
    }
}
