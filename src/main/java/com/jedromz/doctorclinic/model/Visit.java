package com.jedromz.doctorclinic.model;

import com.jedromz.doctorclinic.model.interfaces.IDateTimePeriod;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit implements IDateTimePeriod {

    public Visit(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @FutureOrPresent
    private LocalDateTime start;
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    private boolean confirmed;
    private boolean cancelled;
    private boolean deleted;
    @Version
    private int version;


    @Override
    public LocalDateTime getStartDateTime() {
        return start;
    }

    @Override
    public LocalDateTime getEndDateTime() {
        return end;
    }
}
