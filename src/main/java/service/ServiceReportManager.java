package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import model.ServiceReport;

import java.util.ArrayList;

@Service
public class ServiceReportManager {
    // Map where key is subscriberId and value is list of service reports
    private Map<Integer, List<ServiceReport>> subscriberReports = new HashMap<>();

    // Method to add a report for a specific subscriber
    public void addServiceReport(Integer subscriberID, ServiceReport report) {
        subscriberReports.computeIfAbsent(subscriberID, k -> new ArrayList<>()).add(report);
    }

    // Method to get all reports for a specific subscriber
    public List<ServiceReport> getServiceReports(Integer subscriberID) {
        return subscriberReports.getOrDefault(subscriberID, new ArrayList<>());
    }

    // Method to get the entire map (for further processing or printing)
    public Map<Integer, List<ServiceReport>> getAllSubscriberReports() {
        return subscriberReports;
    }
}
