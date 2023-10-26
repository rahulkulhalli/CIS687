package org.syr.cis687.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.syr.cis687.enums.CurrentState;
import org.syr.cis687.enums.OperatingState;

import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "shuttle")
public class Shuttle {

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
    private Integer currentCapacity;

    @NonNull
    @Column(name = "operating_state")
    @Getter @Setter
    private OperatingState operatingState;

    @NonNull
    @Column(name = "current_state")
    @Getter @Setter
    private CurrentState currentState;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "current_location_fk")
    @Getter @Setter
    private Location currentLocation;

    @Column(name = "departure_time")
    @Getter @Setter
    private Time departureTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "passengers_fk")
    @Getter @Setter
    private List<Passenger> passengerList;
}
