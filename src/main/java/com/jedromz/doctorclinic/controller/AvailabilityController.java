package com.jedromz.doctorclinic.controller;

import com.jedromz.doctorclinic.model.dto.AvailabilityDto;
import com.jedromz.doctorclinic.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<Page<AvailabilityDto>> getAllAvailabilities(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(availabilityService.findAll(pageable)
                .map(s -> modelMapper.map(s, AvailabilityDto.class)));
    }
}
