package com.jedromz.doctorclinic.mapping;

import com.jedromz.doctorclinic.controller.DoctorController;
import com.jedromz.doctorclinic.model.Vacation;
import com.jedromz.doctorclinic.model.dto.VacationDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class VacationToVacationDto implements Converter<Vacation, VacationDto> {
    @Override
    public VacationDto convert(MappingContext<Vacation, VacationDto> mappingContext) {
        Vacation vacation = mappingContext.getSource();
        VacationDto vacationDto = VacationDto.builder()
                .id(vacation.getId())
                .start(vacation.getStart())
                .end(vacation.getEnd())
                .doctorId(vacation.getDoctor().getId())
                .version(vacation.getVersion())
                .build();
        vacationDto.add(linkTo(methodOn(DoctorController.class).getDoctor(vacation.getDoctor().getId()))
                .withRel("doctor-details"));
        return vacationDto;
    }
}
