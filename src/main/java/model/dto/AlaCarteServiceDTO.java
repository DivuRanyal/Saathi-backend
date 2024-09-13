package model.dto;

import java.math.BigDecimal;
import java.sql.Time;

public class AlaCarteServiceDTO {

    private Integer serviceID;
    private String serviceName;
    private String serviceDescription;
    private Integer frequency;
    private String frequencyUnit;
    private BigDecimal priceINR;
    private BigDecimal priceUSD;
    private Time businessHoursStart;
    private Time businessHoursEnd;
    private Integer status;
    private Integer createdBy;
    private Integer updatedBy;
    private Integer durationInHours;
    private BigDecimal surgePrice;
    private String iconName;
    private String extra;

    // Constructors
    public AlaCarteServiceDTO() {}

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
   
    public BigDecimal getPriceINR() {
		return priceINR;
	}

	public void setPriceINR(BigDecimal priceINR) {
		this.priceINR = priceINR;
	}

	public BigDecimal getPriceUSD() {
		return priceUSD;
	}

	public void setPriceUSD(BigDecimal priceUSD) {
		this.priceUSD = priceUSD;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        if (createdBy != null) {
            this.createdBy = createdBy;
        } else {
            this.createdBy = -1;  // or handle null case based on your business needs
        }
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        if (updatedBy != null) {
            this.updatedBy = updatedBy;
        } else {
            this.updatedBy = -1;  // Handle null case
        }
    }

	public Integer getDurationInHours() {
		return durationInHours;
	}

	public void setDurationInHours(Integer durationInHours) {
		this.durationInHours = durationInHours;
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