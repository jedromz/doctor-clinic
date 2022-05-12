package com.jedromz.doctorclinic.mapping;

import com.jedromz.doctorclinic.controller.DoctorController;
import com.jedromz.doctorclinic.model.Doctor;
import com.jedromz.doctorclinic.model.dto.DoctorDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Service
@RequiredArgsConstructor
public class DoctorToDoctorDto implements Converter<Doctor, DoctorDto> {
    @Override
    public DoctorDto convert(MappingContext<Doctor, DoctorDto> mappingContext) {
        Doctor doctor = mappingContext.getSource();
        DoctorDto doctorDto = DoctorDto.builder()
                .id(doctor.getId())
                .firstname(doctor.getFirstname())
                .lastname(doctor.getLastname())
                .nip(doctor.getNip())
                .rate(doctor.getRate())
                .specialization(doctor.getSpecialization())
                .version(doctor.getVersion())
                .isDeleted(doctor.isDeleted())
                .build();
        doctorDto.add(linkTo(methodOn(DoctorController.class).getDoctorsAvailabilities(doctor.getId(), Pageable.unpaged()))
                .withRel("doctors-availabilities"));
        doctorDto.add(linkTo(methodOn(DoctorController.class).getDoctorsVacations(doctor.getId(), Pageable.unpaged()))
                .withRel("doctors-vacations"));
        doctorDto.add(linkTo(methodOn(DoctorController.class).getDoctorsVisits(doctor.getId(), Pageable.unpaged()))
                .withRel("doctors-visits"));
        return doctorDto;
    }
}
