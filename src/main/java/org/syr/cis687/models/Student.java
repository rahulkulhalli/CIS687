package org.syr.cis687.models;

import jakarta.persistence.*;

@Entity
public class Student extends BaseEntity {
    public Student() {
        super();
    }

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
                super.getFirstName(), super.getLastName(), super.getOrgId(), super.getEmailId()
        );
    }
}
