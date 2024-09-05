package model;

import javax.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "Subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SubscriberID")
    private int subscriberID;

    @NotNull(message = "First name is required")
    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "Email")
    private String email;

    @Column(name = "ContactNo")
    private String contactNo;

    @Column(name = "CountryCode")
    private String countryCode;

    @NotNull(message = "Password is required")
    @Column(name = "Password")
    private String password;

    @Column(name = "LastLoginTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @ManyToOne
    @JoinColumn(name = "PackageID", referencedColumnName = "PackageID", foreignKey = @ForeignKey(name = "PackageID"))
    private SubscriptionPackage subscriptionPackage;

    @Column(name = "StartDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "EndDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "BillingStatus")
    private int billingStatus;

    @ManyToOne
    @JoinColumn(name = "SaathiID", referencedColumnName = "AdminUserID", foreignKey = @ForeignKey(name = "SaathiID"))
    private AdminUser saathi;

    @Column(name = "CreatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "LastUpdatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDate;

    @Column(name = "Status")
    private int status;

 // Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public int getSubscriberId() {
		return subscriberID;
	}

	public void setSubscriberId(int subscriberID) {
		this.subscriberID = subscriberID;
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

	public SubscriptionPackage getSubscriptionPackage() {
		return subscriptionPackage;
	}

	public void setSubscriptionPackage(SubscriptionPackage subscriptionPackage) {
		this.subscriptionPackage = subscriptionPackage;
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

	public int getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(int billingStatus) {
		this.billingStatus = billingStatus;
	}

	public AdminUser getSaathi() {
		return saathi;
	}

	public void setSaathi(AdminUser saathi) {
		this.saathi = saathi;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
		@PrePersist
    protected void onCreate() {
        createdDate = new Date();  // Set the current timestamp for createdDate
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = new Date();  // Set the current timestamp for lastUpdatedDate
    }

    // Getters and Setters
    
}
