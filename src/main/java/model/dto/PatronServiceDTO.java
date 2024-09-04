package model.dto;

import model.SubscriberAlaCarteServices;

public class PatronServiceDTO {

    private PatronDTO patronDTO;
    private SubscriberAlaCarteServices subscriberAlaCarteServices;

    // Getters and Setters

    public PatronDTO getPatronDTO() {
        return patronDTO;
    }

    public void setPatronDTO(PatronDTO patronDTO) {
        this.patronDTO = patronDTO;
    }

    public SubscriberAlaCarteServices getSubscriberAlaCarteServices() {
        return subscriberAlaCarteServices;
    }

    public void setSubscriberAlaCarteServices(SubscriberAlaCarteServices subscriberAlaCarteServices) {
        this.subscriberAlaCarteServices = subscriberAlaCarteServices;
    }
}
