package com.jedromz.doctorclinic.model.dto;

import com.jedromz.doctorclinic.model.DoctorSpecialization;
import lombok.*;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto extends RepresentationModel<DoctorDto> {

    private Long id;
    private String firstname;
    private String lastname;
    private String nip;
    @Positive
    private BigDecimal rate;
    private DoctorSpecialization specialization;
    private Integer version;
    private boolean isDeleted;

}
