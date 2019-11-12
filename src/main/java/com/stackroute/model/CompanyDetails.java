package com.stackroute.model;

public class CompanyDetails
{
    private long id;
    private String companyId;
    private String companyName;
    private String emailId;
    private String ownerPhone;
    private String password;

    public CompanyDetails(int id, String companyId, String companyName, String emailId, String ownerPhone, String password) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.emailId = emailId;
        this.ownerPhone = ownerPhone;
        this.password = password;
    }

    public CompanyDetails() {
    }

    public long getId() {
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
