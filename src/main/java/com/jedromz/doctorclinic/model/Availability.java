package com.jedromz.doctorclinic.model;

import com.jedromz.doctorclinic.model.interfaces.ITimePeriod;
import com.jedromz.doctorclinic.validation.annotation.StartBeforeEndTime;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@StartBeforeEndTime
public class Availability implements ITimePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime start;
    private LocalTime end;
    private Integer visitDurationInMinutes;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    private boolean deleted;
    @Version
    private int version;

    @Override
    public LocalTime getStartTime() {
        return start;
    }

    @Override
    public LocalTime getEndTime() {
        return end;
    }
}
