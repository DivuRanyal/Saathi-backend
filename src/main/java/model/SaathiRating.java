package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SaathiRatings")
public class SaathiRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RatingID")
    private int ratingID;

    @ManyToOne
    @JoinColumn(name = "SubscriberID", referencedColumnName = "SubscriberID", foreignKey = @ForeignKey(name = "FK_Subscriber"))
    private Subscriber subscriber;

    @ManyToOne
    @JoinColumn(name = "SaathiID", referencedColumnName = "AdminUserID", foreignKey = @ForeignKey(name = "FK_Saathi"))
    private AdminUser saathi;

    @Column(name = "Rating")
    private int rating;  // Assume ratings are integers between 1 and 5

    @Column(name = "CreatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();  // Set the current timestamp for createdDate
    }

    // Getters and Setters
    public int getRatingID() {
        return ratingID;
    }

    public void setRatingID(int ratingID) {
        this.ratingID = ratingID;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public AdminUser getSaathi() {
        return saathi;
    }

    public void setSaathi(AdminUser saathi) {
        this.saathi = saathi;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
