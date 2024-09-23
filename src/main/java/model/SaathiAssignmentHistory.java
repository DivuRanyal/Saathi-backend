package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SaathiAssignmentHistory")
public class SaathiAssignmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AssignmentID")
    private int assignmentID;

    // Relationship to Subscriber
    @ManyToOne
    @JoinColumn(name = "SubscriberID", referencedColumnName = "SubscriberID", foreignKey = @ForeignKey(name = "SubscriberID"))
    private Subscriber subscriber;

    // Relationship to AdminUser (Saathi)
    @ManyToOne
    @JoinColumn(name = "SaathiID", referencedColumnName = "AdminUserID", foreignKey = @ForeignKey(name = "SaathiID"))
    private AdminUser saathi;

    // Date when the Saathi was assigned
    @Column(name = "AssignmentDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignmentDate;

    // Date when the Saathi's assignment ended
    @Column(name = "EndDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;  // This will be populated when the Saathi is replaced

    // Constructors
    public SaathiAssignmentHistory() {}

    public SaathiAssignmentHistory(Subscriber subscriber, AdminUser saathi) {
        this.subscriber = subscriber;
        this.saathi = saathi;
        this.assignmentDate = new Date();  // Set the current date as assignment date
    }

    // Getters and setters
    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
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

    public Date getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
