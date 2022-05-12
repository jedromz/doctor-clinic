package com.jedromz.doctorclinic.service.impl;

import com.jedromz.doctorclinic.model.Availability;
import com.jedromz.doctorclinic.repository.AvailabilityRepository;
import com.jedromz.doctorclinic.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    @Override
    public Page<Availability> findAll(Pageable pageable) {
        return availabilityRepository.findAll(pageable);
    }
}
