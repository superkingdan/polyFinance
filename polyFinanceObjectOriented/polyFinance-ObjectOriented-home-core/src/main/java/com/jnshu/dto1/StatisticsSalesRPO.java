package com.jnshu.dto1;

/**
 * 获得指定产品销量接收条件对象
 */
public class StatisticsSalesRPO {
    private long id;
    private int page=1;
    private int size=10;
    private Long dateMin;
    private Long dateMax;
    private Long currentDate;
    private Long nextDate;

    public Long getNextDate() {
        return nextDate;
    }

    public void setNextDate(Long nextDate) {
        this.nextDate = nextDate;
    }

    public Long getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Long currentDate) {
        this.currentDate = currentDate;
    }

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

    public Long getDateMin() {
        return dateMin;
    }

    public void setDateMin(Long dateMin) {
        this.dateMin = dateMin;
    }

    public Long getDateMax() {
        return dateMax;
    }

    public void setDateMax(Long dateMax) {
        this.dateMax = dateMax;
    }

    @Override
    public String toString() {
        return "StatisticsSalesRPO{" +
                "id=" + id +
                ", page=" + page +
                ", size=" + size +
                ", dateMin=" + dateMin +
                ", dateMax=" + dateMax +
                ", currentDate=" + currentDate +
                ", nextDate=" + nextDate +
                '}';
    }
}
