package model.dto;

import java.util.List;
import java.util.Map;

public class SubscriberServicesDTO {
    private Integer subscriberID;
    private String subscriberName;  // Add other subscriber fields as needed
    private List<Map<String, Object>> services; // Change to List<Map<String, Object>>

    // Constructor
    public SubscriberServicesDTO(Integer subscriberID, String subscriberName, List<Map<String, Object>> services) {
        this.subscriberID = subscriberID;
        this.subscriberName = subscriberName;
        this.services = services;
    }

    // Getters and Setters
    public Integer getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(Integer subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public List<Map<String, Object>> getServices() {
        return services;
    }

    public void setServices(List<Map<String, Object>> services) {
        this.services = services;
    }
}
