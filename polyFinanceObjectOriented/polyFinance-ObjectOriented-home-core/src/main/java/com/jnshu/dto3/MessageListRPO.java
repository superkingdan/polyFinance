package com.jnshu.dto3;

/**
 * 获得产品列表分页接收数据实体类
 */
public class MessageListRPO {
    private long id;
    private long pageNum;
    private long pageSize;
    private long userId;
    private long createAt;
    private long createBy;
    private long updateAt;
    private long updateBy;
    private String title;
    private String loginName;
    private long createMin;
    private long createMax;
    private long isSent;
    private long sentPersonType;
    private String introduce;
    private String content;
    //发送人群，0：所有人，1：认证投资人

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //消息类型，0：即时发送，1：定时发送
    private int messageType;
    //是否推送，0：不推送，1：推送
    private int isPush;
    //是否发送，0：发送，1：待发送，2：存为草稿
    private long transactionId;

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(long createBy) {
        this.createBy = createBy;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(long updateBy) {
        this.updateBy = updateBy;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getIsPush() {
        return isPush;
    }

    public void setIsPush(int isPush) {
        this.isPush = isPush;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getCreateMin() {
        return createMin;
    }

    public void setCreateMin(long createMin) {
        this.createMin = createMin;
    }

    public long getCreateMax() {
        return createMax;
    }

    public void setCreateMax(long createMax) {
        this.createMax = createMax;
    }

    public long getIsSent() {
        return isSent;
    }

    public void setIsSent(long isSent) {
        this.isSent = isSent;
    }

    public long getSentPersonType() {
        return sentPersonType;
    }

    public void setSentPersonType(long sentPersonType) {
        this.sentPersonType = sentPersonType;
    }

    @Override
    public String toString() {
        return "ProductListRPO{" +
                "userId=" + userId +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", title=" + title +
                ", loginName='" + loginName + '\'' +
                ", createMin=" + createMin +
                ", createMax='" + createMax + '\'' +
                ", isSent='" + isSent + '\'' +
                ", createAt=" + createAt +
                ", createBy=" + createBy +
                ", updateAt=" + updateAt +
                ", updateBy=" + updateBy +
                ", introduce='" + introduce + '\'' +
                ", content='" + content + '\'' +
                ", sentPersonType=" + sentPersonType +
                ", messageType=" + messageType +
                ", isPush=" + isPush +
                ", transactionId=" + transactionId +
                '}';
    }
}