package model.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubscriberDTO {

    private int subscriberID;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;
    private String countryCode;
    private String password;
    private Date lastLoginTime;
    private Integer packageID; // Assume this is the ID only, you can also have the entire object if needed
    private Date startDate;
    private Date endDate;
    private int billingStatus;
    private Integer saathiID; // Assume this is the ID only, you can also have the entire object if needed
    private Date createdDate;
    private Date lastUpdatedDate;
    private int status;
	// Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
	public Integer getPackageID() {
		return packageID;
	}
	public void setPackageID(Integer packageId) {
		this.packageID = packageId;
	}
	
	
	 public String getStartDate() {
	        return startDate != null ? DATE_FORMAT.format(startDate) : null;
	    }

	    // Custom setter for DOB to parse a date string and set the Date object
	    public void setStartDate(String startDate) {
	        try {
	            this.startDate = startDate != null ? DATE_FORMAT.parse(startDate) : null;
	        } catch (ParseException e) {
	            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
	        }
	    }
	    
	    public String getEndDate() {
	        return endDate != null ? DATE_FORMAT.format(endDate) : null;
	    }

	    // Custom setter for DOB to parse a date string and set the Date object
	    public void setEndDate(String endDate) {
	        try {
	            this.endDate = endDate != null ? DATE_FORMAT.parse(endDate) : null;
	        } catch (ParseException e) {
	            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
	        }
	    }
	    
	public Integer getBillingStatus() {
		return billingStatus;
	}
	public void setBillingStatus(Integer billingStatus) {
		this.billingStatus = billingStatus;
	}
	
	public int getSubscriberID() {
		return subscriberID;
	}
	public void setSubscriberID(int subscriberID) {
		this.subscriberID = subscriberID;
	}
	public Integer getSaathiID() {
		return saathiID;
	}
	public void setSaathiID(Integer saathiID) {
		this.saathiID = saathiID;
	}
	// Custom getter for CreatedDate to return a formatted date string
    public String getCreatedDate() {
        return createdDate != null ? DATE_FORMAT.format(createdDate) : null;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    // Custom getter for LastUpdatedDate to return a formatted date string
    public String getLastUpdatedDate() {
        return lastUpdatedDate != null ? DATE_FORMAT.format(lastUpdatedDate) : null;
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
