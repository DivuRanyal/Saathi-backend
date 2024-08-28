package model.dto;

import java.util.Date;

public class SubscriberDTO {

    private int subscriberId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;
    private String countryCode;
    private String password;
    private Date lastLoginTime;
    private Integer packageId; // Assume this is the ID only, you can also have the entire object if needed
    private Date startDate;
    private Date endDate;
    private Integer billingStatus;
    private Integer saathiId; // Assume this is the ID only, you can also have the entire object if needed
    private Date createdDate;
    private Date lastUpdatedDate;
    private Integer status;
	public int getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getBillingStatus() {
		return billingStatus;
	}
	public void setBillingStatus(Integer billingStatus) {
		this.billingStatus = billingStatus;
	}
	public int getSaathiId() {
		return saathiId;
	}
	public void setSaathiId(int saathiId) {
		this.saathiId = saathiId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

    
    // Getters and Setters
}
