package model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@Entity
@Table(name = "SubscriberAlaCarteServices")
public class SubscriberAlaCarteServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SubscriberAlaCarteServicesID")
    private int SubscriberAlaCarteServicesID;

    @Column(name = "SubscriberID")
    private Integer subscriberID;

    @Column(name = "ServiceID")
    private Integer serviceID;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "ServiceDate")
    private Date serviceDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(name = "ServiceTime")
    private Date serviceTime;

    @Column(name = "BillingStatus")
    private Integer billingStatus;

    @Column(name = "CreatedDate")
    private Date createdDate;

    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "IsAccepted")
    private Boolean isAccepted;


	public int getSubscriberAlaCarteServicesID() {
		return SubscriberAlaCarteServicesID;
	}

	public void setSubscriberAlaCarteServicesID(int subscriberAlaCarteServicesID) {
		SubscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
	}

	public Integer getSubscriberID() {
		return subscriberID;
	}

	public void setSubscriberID(Integer subscriberID) {
		this.subscriberID = subscriberID;
	}

	public Integer getServiceID() {
		return serviceID;
	}

	public void setServiceID(Integer serviceID) {
		this.serviceID = serviceID;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public Date getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Integer getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(Integer billingStatus) {
		this.billingStatus = billingStatus;
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

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
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
