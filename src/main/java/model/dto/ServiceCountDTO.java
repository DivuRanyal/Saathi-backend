package model.dto;

import java.util.List;

//DTO to hold the breakdown of services by type
public class ServiceCountDTO {
 private String serviceName;
 private int total;
 private int pending;
 private int completed;

 public ServiceCountDTO(String serviceName) {
     this.serviceName = serviceName;
     this.total = 0;
     this.pending = 0;
     this.completed = 0;
 }

 public void incrementPending() {
     this.pending++;
     this.total++;
 }

 public void incrementCompleted() {
     this.completed++;
     this.total++;
 }

 // Getters and setters
 public String getServiceName() { return serviceName; }
 public int getTotal() { return total; }
 public int getPending() { return pending; }
 public int getCompleted() { return completed; }
}

