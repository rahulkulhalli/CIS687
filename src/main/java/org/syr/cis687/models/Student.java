package org.syr.cis687.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "student")
public class Student {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @NonNull
    @Column(name = "first_name")
    @Getter @Setter private String firstName;

    @NonNull
    @Column(name = "last_name")
    @Getter @Setter private String lastName;

    @NonNull
    @Column(name = "email_id", unique = true)
    @Getter @Setter private String emailId;

    @NonNull
    @Column(name = "contact_number", unique = true)
    @Getter @Setter private String contactNumber;

    @NonNull
    // Organization ID - can be used as Employee ID or Student ID.
    @Column(name = "org_id", unique = true)
    @Getter @Setter private String orgId;

    //Student HAS-A address.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_fk")
    private Location address;

    // Indicates that a student can be a passenger.
    // Student IS-A Passenger.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_fk")
    private Passenger passengerRef;

    @Override
    public String toString() {
        // cannot disclose sensitive information to anyone who issues a GET.
        return String.format(
                "%s %s - SUID: %s, Email: %s",
                this.firstName, this.lastName, this.orgId, this.emailId
        );
    }
}
