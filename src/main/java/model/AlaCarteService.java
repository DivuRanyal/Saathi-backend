package model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "Services")
public class AlaCarteService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ServiceID")
    private Integer serviceID;

    
    @Column(name = "ServiceName",nullable = false)
    private String serviceName;

    // Corrected the typo from "ServiceDesciption" to "ServiceDescription"
    @Column(name = "ServiceDescription", columnDefinition = "TEXT")
    private String serviceDescription;

    @Column(name = "Frequency")
    private Integer frequency;

    @Column(name = "FrequencyUnit")
    private String frequencyUnit; // New field to store the unit of frequency (e.g., Monthly, Weekly)

    @Column(name = "PriceUSD")
    private BigDecimal priceUSD;

    @Column(name = "PriceINR")
    private BigDecimal priceINR;

    @Column(name = "BusinessHoursStart")
    private Time businessHoursStart;

    @Column(name = "BusinessHoursEnd")
    private Time businessHoursEnd;

    @Column(name = "DurationInHours")
    private Integer durationInHours; // New field to store the duration of the service in hours

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "Status")
    private Integer status;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy", referencedColumnName = "AdminUserID")
    @JsonIgnore
    private AdminUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UpdatedBy", referencedColumnName = "AdminUserID")
    private AdminUser updatedBy;

    @Column(name = "SurgePrice") // New field for surge pricing
    private BigDecimal surgePrice;

    @Column(name = "IconName") // New field for storing the icon name
    private String iconName;

    @Column(name = "Extra") // New field for storing extra string data
    private String extra;

    // Constructors
    public AlaCarteService() {}

    // Getters and Setters
    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
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

	public Time getBusinessHoursStart() {
        return businessHoursStart;
    }

    public void setBusinessHoursStart(Time businessHoursStart) {
        this.businessHoursStart = businessHoursStart;
    }

    public Time getBusinessHoursEnd() {
        return businessHoursEnd;
    }

    public void setBusinessHoursEnd(Time businessHoursEnd) {
        this.businessHoursEnd = businessHoursEnd;
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

    public AdminUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AdminUser createdBy) {
        this.createdBy = createdBy;
    }

    public AdminUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AdminUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    
    public Integer getDurationInHours() {
		return durationInHours;
	}

	public void setDurationInHours(Integer durationInHours) {
		this.durationInHours = durationInHours;
	}

	@PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = new Date();
    }

	public BigDecimal getSurgePrice() {
		return surgePrice;
	}

	public void setSurgePrice(BigDecimal surgePrice) {
		this.surgePrice = surgePrice;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
    
    
}
