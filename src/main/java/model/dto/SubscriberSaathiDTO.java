package model.dto;

import model.AdminUser;
import model.Subscriber;

public class SubscriberSaathiDTO {
   private Subscriber subscriber;
    private AdminUser saathi;  // Storing the entire AdminUser object

    // Constructor
    public SubscriberSaathiDTO(Subscriber subscriber, AdminUser saathi) {
      this.subscriber= subscriber;
        this.saathi = saathi;
    }


    public AdminUser getSaathi() {
        return saathi;
    }

    public void setSaathi(AdminUser saathi) {
        this.saathi = saathi;
    }


	public Subscriber getSubscriber() {
		return subscriber;
	}


	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
    
    
}
