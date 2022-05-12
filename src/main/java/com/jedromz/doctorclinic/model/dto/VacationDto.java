package com.jedromz.doctorclinic.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationDto extends RepresentationModel<VacationDto> {

    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Long doctorId;
    private Integer version;
}
