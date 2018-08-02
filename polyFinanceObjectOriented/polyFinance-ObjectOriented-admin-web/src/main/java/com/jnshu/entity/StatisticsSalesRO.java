package com.jnshu.entity;

public class StatisticsSalesRO {

    private long date;
    private long peopleCounting;
    private long numberOfTimes;
    private String totalMoney="";
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getPeopleCounting() {
        return peopleCounting;
    }

    public void setPeopleCounting(long peopleCounting) {
        this.peopleCounting = peopleCounting;
    }

    public long getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(long numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }


    @Override
    public String toString() {
        return "StatisticsSalesRO{" +
                ", date=" + date +
                ", peopleCounting=" + peopleCounting +
                ", numberOfTimes=" + numberOfTimes +
                ", totalMoney='" + totalMoney + '\'' +
                ", total=" + total +
                '}';
    }
}
