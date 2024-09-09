package model;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "PackageServices")
public class PackageServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PackageServicesID")
    private Integer packageServicesID;

    @ManyToOne
    @JoinColumn(name = "PackageID")
    private SubscriptionPackage subscriptionPackage;

 // Add a service reference if applicable
    @ManyToOne
    @JoinColumn(name = "ServiceID")
    private AlaCarteService service;
    
    @Column(name = "Frequency")
    private Integer frequency;

    @Column(name = "FrequencyUnit")
    private String frequencyUnit;

    @Column(name = "PriceUSD")
    private BigDecimal priceUSD;

    @Column(name = "PriceINR")
    private BigDecimal priceINR;

    @Column(name = "CreatedDate")
    private Date createdDate;

    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "UpdatedBy")
    private Integer updatedBy;

    @ManyToOne
    private Subscriber subscriber;
    
    
    public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
	public Integer getPackageServicesID() {
		return packageServicesID;
	}

	public void setPackageServicesID(Integer packageServicesID) {
		this.packageServicesID = packageServicesID;
	}

	public SubscriptionPackage getSubscriptionPackage() {
		return subscriptionPackage;
	}

	public void setSubscriptionPackage(SubscriptionPackage subscriptionPackage) {
		this.subscriptionPackage = subscriptionPackage;
	}

	

	public AlaCarteService getService() {
		return service;
	}

	public void setService(AlaCarteService service) {
		this.service = service;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getFrequencyUnit() {
		return frequencyUnit;
	}

	public void setFrequencyUnit(String frequencyUnit) {
		this.frequencyUnit = frequencyUnit;
	}

	public BigDecimal getPriceUSD() {
		return priceUSD;
	}

	public void setPriceUSD(BigDecimal priceUSD) {
		this.priceUSD = priceUSD;
	}

	public BigDecimal getPriceINR() {
		return priceINR;
	}

	public void setPriceINR(BigDecimal priceINR) {
		this.priceINR = priceINR;
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
