package com.jnshu.entity;

/**
 * 获得指定产品销量接收条件对象
 */
public class StatisticsSalesRPO {
    private long id;
    private int page=1;
    private int size=10;
    private long dateMin;
    private long dateMax;

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

    public long getDateMin() {
        return dateMin;
    }

    public void setDateMin(long dateMin) {
        this.dateMin = dateMin;
    }

    public long getDateMax() {
        return dateMax;
    }

    public void setDateMax(long dateMax) {
        this.dateMax = dateMax;
    }
}
