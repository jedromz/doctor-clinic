package com.jedromz.doctorclinic.repository;

import com.jedromz.doctorclinic.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
}
