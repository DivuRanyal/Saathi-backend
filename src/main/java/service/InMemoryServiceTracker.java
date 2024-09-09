package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import model.ServiceReport;
@Component
public class InMemoryServiceTracker {

    // Map to track services for each subscriber
    private Map<Long, List<ServiceReport>> subscriberServiceMap = new HashMap<>();

    // Start tracking services for a subscriber when they purchase a package
    public void startTracking(Long subscriberId, List<ServiceReport> serviceReports) {
        subscriberServiceMap.put(subscriberId, serviceReports);
    }

    // Get services currently being tracked for a subscriber
    public List<ServiceReport> getTrackedServices(Long subscriberId) {
        return subscriberServiceMap.get(subscriberId);
    }
    
 // Method to update tracking for a subscriber's services
    public void updateTracking(Long subscriberId, List<ServiceReport> updatedServices) {
        if (subscriberServiceMap.containsKey(subscriberId)) {
            subscriberServiceMap.put(subscriberId, updatedServices); // Replace with updated services
            System.out.println("In-memory service tracking updated for subscriber ID: " + subscriberId);
        } else {
            System.out.println("No tracking information found for subscriber ID: " + subscriberId);
        }
    }
}
