package service;

import model.SubscriberAlaCarteServices;

import java.util.List;

public interface SubscriberAlaCarteServicesService {
    List<SubscriberAlaCarteServices> getAllServices();
    SubscriberAlaCarteServices getServiceById(int id);
    SubscriberAlaCarteServices createOrUpdateService(SubscriberAlaCarteServices service);
    void deleteService(int id);
    // Ensure this method matches the implementation
    Integer getSubscriberAlaCarteServicesID(Integer subscriberId, Integer serviceId);
   
}
