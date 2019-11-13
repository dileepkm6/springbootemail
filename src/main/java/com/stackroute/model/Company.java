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
    @OneToOne(targetEntity = DAOUser.class,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name = "user_id")
    private DAOUser DAOUser;

    public Company(String companyId, String companyName, DAOUser DAOUser) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.DAOUser = DAOUser;
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

    public DAOUser getDAOUser() {
        return DAOUser;
    }

    public void setDAOUser(DAOUser DAOUser) {
        this.DAOUser = DAOUser;
    }
}
