package org.syr.cis687.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @Column(name = "username")
    @Getter @Setter private String username;

    @NonNull
    @Column(name = "password")
    @Getter @Setter private String password;


    @NonNull
    @Column(name = "contact_number", unique = true)
    @Getter @Setter private String contactNumber;

    @NonNull
    // Organization ID - can be used as Employee ID or Student ID.
    @Column(name = "org_id", unique = true)
    @Getter @Setter private String orgId;

    //Student HAS-A address.
    @Getter @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_fk")
    @JsonProperty("address")
    private Location address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shuttle_id")
    @JsonBackReference
    @Getter @Setter
    private Shuttle shuttle;

    @Getter @Setter
    private Boolean hasBoarded = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    public Student(){}
    public Student(String firstName, String lastName, String emailId, String username, String password, String contactNumber, String orgId, Location address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.username = username;
        this.password = password;
        this.contactNumber = contactNumber;
        this.orgId = orgId;
        this.address = address;
    }
    @Override
    public String toString() {
        return String.format(
                "Student[id=%s, orgId=%s, firstName=%s, lastName=%s, emailId=%s]",
                this.id, this.orgId, this.firstName, this.lastName, this.emailId
        );
    }

    @Override
    public boolean equals(Object obj) {

        try {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            Student otherStudent = (Student) obj;

            return (otherStudent.getId().equals(this.id)
                    && otherStudent.getFirstName().equals(this.firstName)
                    && otherStudent.getLastName().equals(this.lastName)
                    && otherStudent.getEmailId().equals(this.emailId)
                    && otherStudent.getAddress().equals(this.address)
                    && otherStudent.getContactNumber().equals(this.contactNumber)
                    && otherStudent.getOrgId().equals(this.orgId));

        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id, this.firstName, this.lastName,
                this.address, this.emailId, this.contactNumber, this.orgId
        );
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
