package org.syr.cis687.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This Entity object will be act as the Base class for the three entities in this project:
 * Driver, Emergency Contacts, Student.
 */
@Entity
public abstract class BaseEntity {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter private Long id;

    @NonNull
    @Column(name = "first_name")
    @Getter @Setter private String firstName;

    @NonNull
    @Column(name = "last_name")
    @Getter @Setter private String lastName;

    @NonNull
    @Column(name = "email_id")
    @Getter @Setter private String emailId;

    @NonNull
    @Column(name = "contact_number")
    @Getter @Setter private String contactNumber;

    @NonNull
    // Organization ID - can be used as Employee ID or Student ID.
    @Column(name = "org_id")
    @Getter @Setter private String orgId;

    @Override
    public String toString() {
        return String.format(
                "%s %s - Organization ID: %s, Contact number: %s, Email: %s",
                this.firstName, this.lastName, this.orgId, this.contactNumber, this.emailId
        );
    }
}
