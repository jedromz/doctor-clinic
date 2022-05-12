package com.jedromz.doctorclinic.mapping;

import com.jedromz.doctorclinic.controller.DoctorController;
import com.jedromz.doctorclinic.model.Availability;
import com.jedromz.doctorclinic.model.dto.AvailabilityDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class AvailabilityToAvailabilityDto implements Converter<Availability, AvailabilityDto> {
    @Override
    public AvailabilityDto convert(MappingContext<Availability, AvailabilityDto> mappingContext) {
        Availability availability = mappingContext.getSource();
        AvailabilityDto availabilityDto = AvailabilityDto.builder()
                .id(availability.getId())
                .dayOfWeek(availability.getDayOfWeek())
                .start(availability.getStart())
                .end(availability.getEnd())
                .visitDurationInMinutes(availability.getVisitDurationInMinutes())
                .doctorId(availability.getDoctor().getId())
                .version(availability.getVersion())
                .build();
        availabilityDto.add(linkTo(methodOn(DoctorController.class).getDoctor(availability.getDoctor().getId()))
                .withRel("doctor-details"));
        return availabilityDto;
    }
}
