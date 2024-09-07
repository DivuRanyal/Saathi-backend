package model.dto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SubscriptionPackageDTO {

	 private Integer packageID; 
    private String packageName;
    private String packageDescription;
    private BigDecimal priceUSD;
    private BigDecimal priceINR;
    private Integer status;
    private Integer createdBy;  // Add createdBy in DTO
    private Integer updatedBy;  // Add updatedBy in DTO
    private Date createdDate; // Add this field
    private Date lastUpdatedDate; // Add this field

 // Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Integer getPackageID() {
		return packageID;
	}
	public void setPackageID(Integer packageID) {
		this.packageID = packageID;
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
	private List<PackageServiceDTO> packageServices;
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getPackageDescription() {
		return packageDescription;
	}
	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
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
	public List<PackageServiceDTO> getPackageServices() {
		return packageServices;
	}
	public void setPackageServices(List<PackageServiceDTO> packageServices) {
		this.packageServices = packageServices;
	}

    
    // Getters and Setters
}
