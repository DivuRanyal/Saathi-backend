package model.dto;

import java.math.BigDecimal;
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

    public Integer getPackageID() {
		return packageID;
	}
	public void setPackageID(Integer packageID) {
		this.packageID = packageID;
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
