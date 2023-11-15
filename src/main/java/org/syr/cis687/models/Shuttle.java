package org.syr.cis687.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.syr.cis687.enums.CurrentState;
import org.syr.cis687.enums.OperatingState;

import java.sql.Time;
import java.util.List;

@Entity
@ToString
@Table(name = "shuttle")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shuttle {
    @Getter
    private static final Location DEFAULT_LOCATION = new Location(43.03720157575044, -76.1315052496461);

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shuttle_id")
    @Getter @Setter
    private Long shuttleId;

    @Column(name = "alias", unique = true)
    @Getter @Setter
    private String alias;

    @NonNull
    @Column(name = "max_capacity")
    @Getter @Setter
    private Integer maxCapacity;

    @NonNull
    @Column(name = "current_capacity")
    @Getter @Setter
    private Integer currentCapacity = 0;

    @NonNull
    @Column(name = "operating_state")
    @Getter @Setter
    private OperatingState operatingState;

    @NonNull
    @Column(name = "current_state")
    @Getter @Setter
    private CurrentState currentState;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "current_location_fk")
    @JsonProperty("current_location")
    @Getter @Setter
    private Location currentLocation;

    @Column(name = "departure_time")
    @Getter @Setter
    private Time departureTime;

    @Column(name = "arrival_time")
    @Getter @Setter
    private Time arrivalTime;

    @Column(name = "has_departed")
    @Getter @Setter
    private Boolean hasDepartedFromStop = false;

    @Column(name = "has_arrived")
    @Getter @Setter
    private Boolean hasArrivedAtStop = false;

    @Column(name = "shuttle_speed")
    @Getter @Setter
    private Double currentSpeed = 18.0;

    @Getter @Setter
    private Long timeSinceLastStop = 0L;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "shuttle", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Getter @Setter
    private List<Student> passengerList;
}
