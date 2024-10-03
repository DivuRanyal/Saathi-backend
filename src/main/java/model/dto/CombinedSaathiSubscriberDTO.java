package model.dto;

import java.util.Map;

public class CombinedSaathiSubscriberDTO {
    private Map<String, Long> saathiCounts;
    private SaathiServiceSummaryDTO saathiServiceSummary;
    private Map<String, Long> subscriberCounts;
	public Map<String, Long> getSaathiCounts() {
		return saathiCounts;
	}
	public void setSaathiCounts(Map<String, Long> saathiCounts) {
		this.saathiCounts = saathiCounts;
	}
	public SaathiServiceSummaryDTO getSaathiServiceSummary() {
		return saathiServiceSummary;
	}
	public void setSaathiServiceSummary(SaathiServiceSummaryDTO saathiServiceSummary) {
		this.saathiServiceSummary = saathiServiceSummary;
	}
	public Map<String, Long> getSubscriberCounts() {
		return subscriberCounts;
	}
	public void setSubscriberCounts(Map<String, Long> subscriberCounts) {
		this.subscriberCounts = subscriberCounts;
	}

    
    // Getters and Setters
}
