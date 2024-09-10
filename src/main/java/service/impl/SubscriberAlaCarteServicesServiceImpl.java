package service.impl;

import model.AlaCarteService;
import model.SubscriberAlaCarteServices;
import repository.AlaCarteServiceRepository;
import repository.InteractionRepository;
import repository.SubscriberAlaCarteServicesRepository;
import repository.SubscriberRepository;
import service.SubscriberAlaCarteServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriberAlaCarteServicesServiceImpl implements SubscriberAlaCarteServicesService {

    @Autowired
    private SubscriberAlaCarteServicesRepository servicerepository;
    
    @Autowired
    private AlaCarteServiceRepository repository;
    
    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private SubscriberAlaCarteServicesRepository subscriberAlaCarteServicesRepository;

    @Override
    public List<SubscriberAlaCarteServices> getAllServices() {
        return servicerepository.findAll();
    }

    @Override
    public SubscriberAlaCarteServices getServiceById(int id) {
        Optional<SubscriberAlaCarteServices> service = servicerepository.findById(id);
        return service.orElse(null);
    }

    @Override
    public SubscriberAlaCarteServices createOrUpdateService(SubscriberAlaCarteServices serviceRequest) {
        // Validate that the serviceID exists in the Services table
        Optional<AlaCarteService> service = repository.findById(serviceRequest.getServiceID());

        if (!service.isPresent()) {
            throw new IllegalArgumentException("Invalid serviceID: Service does not exist");
        }

        // Proceed with saving the SubscriberAlaCarteServices entity
        return servicerepository.save(serviceRequest);
    }

    @Override
    public void deleteService(int id) {
    	servicerepository.deleteById(id);
    }
/*    
    @Override
    public List<Object[]> getAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID) {
        return interactionRepository.findAlaCarteServicesWithCompletionStatusAndServiceDetails(subscriberID);
    }
   */
    
    @Override
    public Integer getSubscriberAlaCarteServicesID(Long subscriberId, Integer serviceId) {
        // Fetch the SubscriberAlaCarteService entity using subscriberId and serviceId
        Optional<SubscriberAlaCarteServices> subscriberAlaCarteService = subscriberAlaCarteServicesRepository.findBySubscriberIDAndServiceID(subscriberId, serviceId);

        // Return the SubscriberAlaCarteServicesID if present, otherwise return null
        return subscriberAlaCarteService.map(SubscriberAlaCarteServices::getSubscriberAlaCarteServicesID).orElse(null);
    }
    
    
}
