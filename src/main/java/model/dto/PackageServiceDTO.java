package model.dto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PackageServiceDTO {

	private Integer packageServiceID;
    private Integer serviceID;
    private Integer packageID;
    private String packageName;
    private String serviceName;
    private Integer frequency;
    private String frequencyUnit;
    private BigDecimal priceUSD;
    private BigDecimal priceINR;
    private Integer status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date lastUpdatedDate;
    private Integer createdBy;  // Add createdBy in DTO
    private Integer updatedBy;
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

	// Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public Integer getServiceID() {
		return serviceID;
	}
	public void setServiceID(Integer serviceID) {
		this.serviceID = serviceID;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreatedDate() {
        return createdDate != null ? DATE_FORMAT.format(createdDate) : null;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public String getLastUpdatedDate() {
    	return lastUpdatedDate != null ? DATE_FORMAT.format(lastUpdatedDate) : null;
       
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    public Integer getPackageServiceID() {	
    	        return packageServiceID;
    	        
    }
    
	public void setPackageServiceID(Integer packageServiceID) {
		this.packageServiceID = packageServiceID;
	}
    // Getters and Setters
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getPackageID() {
		return packageID;
	}
	public void setPackageID(Integer packageID) {
		this.packageID = packageID;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
    
	
}
