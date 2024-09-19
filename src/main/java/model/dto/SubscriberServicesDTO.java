package model.dto;

import java.util.List;

import model.ServiceReport;
public class SubscriberServicesDTO {
    private Integer subscriberID;
    private String subscriberName;  // Add other subscriber fields as needed
    private List<ServiceReport> services;

    // Constructor
    public SubscriberServicesDTO(Integer subscriberID, String subscriberName, List<ServiceReport> services) {
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

    public List<ServiceReport> getServices() {
        return services;
    }

    public void setServices(List<ServiceReport> services) {
        this.services = services;
    }
}
