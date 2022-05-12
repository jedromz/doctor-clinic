package com.jedromz.doctorclinic.service;

import com.jedromz.doctorclinic.model.Availability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AvailabilityService {
    Page<Availability> findAll(Pageable pageable);
}
