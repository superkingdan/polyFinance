package com.jnshu.Domain2;

public class DomainUserFrontDetail {
    private Long id;
    private String userNumber;
    private Long createAt;
    private String referrerId;
    private Integer realStatus;
    private String realName;
    private String idCard;
    private String phoneNumber;
    private String email;
    private String address;
    private String property;
    private String cumulativeIncome;
    private String frontCard;
    private String reverseCard;
    private Integer applicationStatus;
    private Long defaultCard;

    public DomainUserFrontDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(Integer realStatus) {
        this.realStatus = realStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCumulativeIncome() {
        return cumulativeIncome;
    }

    public void setCumulativeIncome(String cumulativeIncome) {
        this.cumulativeIncome = cumulativeIncome;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public String getFrontCard() {
        return frontCard;
    }

    public void setFrontCard(String frontCard) {
        this.frontCard = frontCard;
    }

    public String getReverseCard() {
        return reverseCard;
    }

    public void setReverseCard(String reverseCard) {
        this.reverseCard = reverseCard;
    }

    public Integer getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(Integer applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Long getDefaultCard() {
        return defaultCard;
    }

    public void setDefaultCard(Long defaultCard) {
        this.defaultCard = defaultCard;
    }

    @Override
    public String toString() {
        return "DomainUserFrontDetail{" +
                "id=" + id +
                ", userNumber='" + userNumber + '\'' +
                ", realStatus=" + realStatus +
                ", realName='" + realName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createAt=" + createAt +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", property='" + property + '\'' +
                ", cumulativeIncome='" + cumulativeIncome + '\'' +
                ", referrerId='" + referrerId + '\'' +
                ", frontCard='" + frontCard + '\'' +
                ", reverseCard='" + reverseCard + '\'' +
                ", applicationStatus=" + applicationStatus +
                ", defaultCard=" + defaultCard +
                '}';
    }
}
