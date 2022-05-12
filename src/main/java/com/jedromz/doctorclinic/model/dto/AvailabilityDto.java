package com.jedromz.doctorclinic.model.dto;


import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Positive;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDto extends RepresentationModel<AvailabilityDto> {
    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime start;
    private LocalTime end;
    @Positive
    private Integer visitDurationInMinutes;
    private Long doctorId;
    private Integer version;
}
