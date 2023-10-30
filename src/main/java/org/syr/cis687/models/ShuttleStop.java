package org.syr.cis687.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "shuttle_stop")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ShuttleStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NonNull
    private Long id;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "stop_location_fk")
    private Location shuttleStopLocation;

    // Any other Shuttle Stop attributes?
}
