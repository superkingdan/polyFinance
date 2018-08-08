package com.jnshu.Domain2;

/**
 * Created by Administrator on 2018/8/7.
 */
public class DomainRoleBack {
    private Long id;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "DomainRoleBack{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}
