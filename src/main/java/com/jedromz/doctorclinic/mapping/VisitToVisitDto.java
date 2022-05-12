package com.jedromz.doctorclinic.mapping;


import com.jedromz.doctorclinic.controller.DoctorController;
import com.jedromz.doctorclinic.controller.PatientController;
import com.jedromz.doctorclinic.model.Visit;
import com.jedromz.doctorclinic.model.dto.VisitDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class VisitToVisitDto implements Converter<Visit, VisitDto> {

    @Override
    public VisitDto convert(MappingContext<Visit, VisitDto> mappingContext) {
        Visit visit = mappingContext.getSource();
        VisitDto visitDto = VisitDto.builder()
                .id(visit.getId())
                .startTime(visit.getStart())
                .endTime(visit.getEndDateTime())
                .confirmed(visit.isConfirmed())
                .cancelled(visit.isCancelled())
                .version(visit.getVersion())
                .build();
        visitDto.add(linkTo(methodOn(PatientController.class).getPatient(visit.getPatient().getId()))
                .withRel("patient-details"));
        visitDto.add(linkTo(methodOn(DoctorController.class).getDoctor(visit.getDoctor().getId()))
                .withRel("doctor-details"));
        return visitDto;
    }
}
