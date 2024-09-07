package model.dto;

import java.math.BigDecimal;

public class PackageServiceDTO {

    private Integer serviceID;
    private Integer frequency;
    private String frequencyUnit;
    private BigDecimal priceUSD;
    private BigDecimal priceINR;
    private Integer status;
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

    
    // Getters and Setters
    
}
