package org.syr.cis687.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

/**
 * This table will be populated once using a startup script.
 * The ID refers to the day of week. Since ID starts from 1,
 * Mon=1, Tue=2, ..., Sun=7
 */
@Entity
@Table(name = "shuttle_schedule")
public class ShuttleSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NonNull
    @Getter @Setter
    private Long scheduleId;

    @Column(name = "start_time")
    @NonNull
    @Getter @Setter
    private Date startTime;

    @Column(name = "end_time")
    @NonNull
    @Getter @Setter
    private Date endTime;
}
