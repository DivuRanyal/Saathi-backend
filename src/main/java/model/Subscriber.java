package model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Subscribers")
public class Subscriber {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "SubscriberID")
	    @JsonProperty("subscriberID")  // Explicitly control the JSON property name
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

    @NotNull(message = "Contact number is required")
    @Column(name = "ContactNo")
    private String contactNo;

    @Column(name = "CountryCode")
    private String countryCode;

    @NotNull(message = "Password is required")
    @Column(name = "Password")
    @JsonIgnore
    private String password;

    @Column(name = "LastLoginTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @ManyToOne
    @JoinColumn(name = "PackageID", referencedColumnName = "PackageID", foreignKey = @ForeignKey(name = "PackageID"))
    @JsonIgnore
    private SubscriptionPackage subscriptionPackage;

    // OneToMany relationship with PackageServices
    @OneToMany(mappedBy = "subscriptionPackage", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PackageServices> packageServices;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SaathiAssignmentHistory> saathiAssignmentHistoryList = new ArrayList<>();

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
    @JsonIgnore
    private AdminUser saathi;

    @Column(name = "CreatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "LastUpdatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDate;

    @Column(name = "Status")
    private int status;

    @Column(name = "Comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "ReasonForChange", columnDefinition = "TEXT")
    private String reasonForChange;

    @OneToOne(mappedBy = "subscriber", cascade = CascadeType.ALL)
    @JsonIgnore
    private CreditCard creditCard;
 // Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	
	public int getSubscriberID() {
		return subscriberID;
	}

	public void setSubscriberID(int subscriberID) {
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
	
	
		public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

		@PrePersist
    protected void onCreate() {
        createdDate = new Date();  // Set the current timestamp for createdDate
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = new Date();  // Set the current timestamp for lastUpdatedDate
    }

	public SubscriptionPackage getSubscriptionPackage() {
		return subscriptionPackage;
	}

	public void setSubscriptionPackage(SubscriptionPackage subscriptionPackage) {
		this.subscriptionPackage = subscriptionPackage;
	}

	public String getReasonForChange() {
		return reasonForChange;
	}

	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public List<PackageServices> getPackageServices() {
		return packageServices;
    }
	
	public void setPackageServices(List<PackageServices> packageServices) {
			this.packageServices = packageServices;
	}
	
	// Modify the setter for Saathi to track Saathi changes
	public void setSaathi(AdminUser newSaathi) {
	    // Check if the list is not empty before trying to access the last element
	    if (!this.saathiAssignmentHistoryList.isEmpty() && this.saathi != null && !this.saathi.equals(newSaathi)) {
	        // Update the EndDate for the previous Saathi assignment
	        SaathiAssignmentHistory lastAssignment = this.saathiAssignmentHistoryList.get(this.saathiAssignmentHistoryList.size() - 1);
	        lastAssignment.setEndDate(new Date());  // Set the current date as the EndDate for the previous Saathi
	    }

	    // Assign the new Saathi
	    if (this.saathi != newSaathi) {
	        this.saathi = newSaathi;
	        // Create a new SaathiAssignmentHistory record for the new Saathi
	        SaathiAssignmentHistory newAssignment = new SaathiAssignmentHistory(this, newSaathi);
	        this.saathiAssignmentHistoryList.add(newAssignment);  // Add to the history list
	    }
	}


    public List<SaathiAssignmentHistory> getSaathiAssignmentHistoryList() {
        return saathiAssignmentHistoryList;
    }

    public void setSaathiAssignmentHistoryList(List<SaathiAssignmentHistory> saathiAssignmentHistoryList) {
        this.saathiAssignmentHistoryList = saathiAssignmentHistoryList;
    }
    // Getters and Setters
   
}
