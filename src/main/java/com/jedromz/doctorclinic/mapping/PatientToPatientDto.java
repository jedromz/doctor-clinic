package com.jedromz.doctorclinic.mapping;

import com.jedromz.doctorclinic.controller.PatientController;
import com.jedromz.doctorclinic.model.Patient;
import com.jedromz.doctorclinic.model.dto.PatientDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class PatientToPatientDto implements Converter<Patient, PatientDto> {
    @Override
    public PatientDto convert(MappingContext<Patient, PatientDto> mappingContext) {
        Patient patient = mappingContext.getSource();
        PatientDto patientDto = PatientDto.builder()
                .id(patient.getId())
                .firstname(patient.getFirstname())
                .lastname(patient.getLastname())
                .birthdate(patient.getBirthdate())
                .version(patient.getVersion())
                .active(patient.isDeleted())
                .email(patient.getEmail())
                .build();
        patientDto.add(linkTo(methodOn(PatientController.class).getPatientsVisits(patient.getId(), Pageable.unpaged()))
                .withRel("patients-visits"));
        return patientDto;
    }
}
