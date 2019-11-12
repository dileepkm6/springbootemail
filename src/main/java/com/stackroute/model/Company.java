package com.stackroute.model;

import javax.persistence.*;

@Entity
public class Company
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String companyId;
    private String companyName;
    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name = "user_id")
    private User user;

    public Company(String companyId, String companyName, User user) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.user = user;
    }

    public Company() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
