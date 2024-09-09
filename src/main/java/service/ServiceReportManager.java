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
    private Map<Long, List<ServiceReport>> subscriberReports = new HashMap<>();

    // Method to add a report for a specific subscriber
    public void addServiceReport(Long subscriberId, ServiceReport report) {
        subscriberReports.computeIfAbsent(subscriberId, k -> new ArrayList<>()).add(report);
    }

    // Method to get all reports for a specific subscriber
    public List<ServiceReport> getServiceReports(Long subscriberId) {
        return subscriberReports.getOrDefault(subscriberId, new ArrayList<>());
    }

    // Method to get the entire map (for further processing or printing)
    public Map<Long, List<ServiceReport>> getAllSubscriberReports() {
        return subscriberReports;
    }
}