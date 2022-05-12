package com.jedromz.doctorclinic.repository;

import com.jedromz.doctorclinic.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}
