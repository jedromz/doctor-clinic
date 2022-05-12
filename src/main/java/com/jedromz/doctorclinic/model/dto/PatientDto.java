package com.jedromz.doctorclinic.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto extends RepresentationModel<PatientDto> {

    private Long id;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String email;
    private boolean active;
    private Integer version;

}
