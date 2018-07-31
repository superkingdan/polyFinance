package com.jnshu.dto1;

/**
 * 用户获得投资金额之后返回的银行卡实体类
 */
public class BankCardRO {
    private long id;
    private String bankName;
    private String bankCard;
    private int cardOrder;
    private String icon;
    private String singleLimited;
    private String dayLimited;

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

    public int getCardOrder() {
        return cardOrder;
    }

    public void setCardOrder(int cardOrder) {
        this.cardOrder = cardOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSingleLimited() {
        return singleLimited;
    }

    public void setSingleLimited(String singleLimited) {
        this.singleLimited = singleLimited;
    }

    public String getDayLimited() {
        return dayLimited;
    }

    public void setDayLimited(String dayLimited) {
        this.dayLimited = dayLimited;
    }

    @Override
    public String toString() {
        return "BankCardRO{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", cardOrder=" + cardOrder +
                ", icon='" + icon + '\'' +
                ", singleLimited='" + singleLimited + '\'' +
                ", dayLimited='" + dayLimited + '\'' +
                '}';
    }
}
