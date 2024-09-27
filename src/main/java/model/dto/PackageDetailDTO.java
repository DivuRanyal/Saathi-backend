package model.dto;

public class PackageDetailDTO {
    private String packageName;
    private int subscriberCount;

    public PackageDetailDTO(String packageName, int subscriberCount) {
        this.packageName = packageName;
        this.subscriberCount = subscriberCount;
    }

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getSubscriberCount() {
		return subscriberCount;
	}

	public void setSubscriberCount(int subscriberCount) {
		this.subscriberCount = subscriberCount;
	}

    
    // Getters and setters...
}
