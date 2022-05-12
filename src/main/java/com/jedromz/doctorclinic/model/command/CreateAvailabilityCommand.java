package com.jedromz.doctorclinic.model.command;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAvailabilityCommand {
    private DayOfWeek dayOfWeek;
    private LocalTime start;
    private LocalTime end;
    private Long doctorId;

}
