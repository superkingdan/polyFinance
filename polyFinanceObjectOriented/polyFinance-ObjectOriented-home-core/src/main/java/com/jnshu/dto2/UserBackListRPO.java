package com.jnshu.dto2;

public class UserBackListRPO {
    private String role;
    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserBackListRPO{" +
                "role='" + role + '\'' +
                ", loginName='" + loginName + '\'' +
                '}';
    }
}
