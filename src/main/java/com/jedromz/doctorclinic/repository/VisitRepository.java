package com.jedromz.doctorclinic.repository;

import com.jedromz.doctorclinic.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    Page<Visit> findByDoctor_Id(Long id, Pageable pageable);
}
