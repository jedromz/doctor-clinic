package com.jedromz.doctorclinic.repository;

import com.jedromz.doctorclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Patient p left join fetch p.visits where p.id = ?1")
    Optional<Patient> findPatientWithVisits(long id);

    boolean existsByEmail(String email);
}
