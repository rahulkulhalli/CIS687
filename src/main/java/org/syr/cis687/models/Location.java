package org.syr.cis687.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    // Every student will have an address, and the shuttle will also have two addresses.
    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private Student student;

    @OneToOne(mappedBy = "currentLocation", fetch = FetchType.EAGER)
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
