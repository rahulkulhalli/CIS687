package org.syr.cis687.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "location")
public class Location {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter private Long id;

    @NonNull
    @Column(name = "longitude")
    @Getter @Setter private Double longitude;

    @NonNull
    @Column(name = "latitude")
    @Getter @Setter private Double latitude;

    @NonNull
    @Column(name = "last_updated")
    @Getter @Setter private Date lastUpdated;

    // Every student will have an address, and the shuttle will also have two addresses.
    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private Student student;

    @OneToOne(mappedBy = "currentLocation", fetch = FetchType.LAZY)
    private Shuttle shuttle;

    @Override
    public String toString() {
        return "Latitude: " +
                this.latitude +
                ", " +
                "Longitude: " +
                this.longitude;
    }
}
