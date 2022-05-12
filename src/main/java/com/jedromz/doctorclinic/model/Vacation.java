package com.jedromz.doctorclinic.model;

import com.jedromz.doctorclinic.model.interfaces.IDateTimePeriod;
import com.jedromz.doctorclinic.validation.annotation.StartBeforeEndDate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@StartBeforeEndDate
public class Vacation implements IDateTimePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @FutureOrPresent
    private LocalDate start;
    @Future
    private LocalDate end;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    private boolean deleted;
    @Version
    private int version;
    @Override
    public LocalDateTime getStartDateTime() {
        return start.atStartOfDay();
    }

    @Override
    public LocalDateTime getEndDateTime() {
        return end.atTime(LocalTime.MAX);
    }
}
