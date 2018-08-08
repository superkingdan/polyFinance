package com.jnshu.dto3;

/**
 * 银行卡列表
 */
public class BankCardList {
    private long id;
    private String bankName;
    private String bankCard;
    private String icon;
    private String singLimited;
    private String dayLimited;
    private String bankPhone;
    private String city;

    public String getBankPhone() {
        return bankPhone;
    }

    public void setBankPhone(String bankPhone) {
        this.bankPhone = bankPhone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSingLimited() {
        return singLimited;
    }

    public void setSingLimited(String singLimited) {
        this.singLimited = singLimited;
    }

    public String getDayLimited() {
        return dayLimited;
    }

    public void setDayLimited(String dayLimited) {
        this.dayLimited = dayLimited;
    }

    @Override
    public String toString() {
        return "BankCardList{" +
                "id=" + id +
                ", bankName=" + bankName +
                ", bankCard='" + bankCard + '\'' +
                ", icon=" + icon +
                ", singLimited='" + singLimited + '\'' +
                ", dayLimited='" + dayLimited + '\'' +
                ", bankPhone='" + bankPhone + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
