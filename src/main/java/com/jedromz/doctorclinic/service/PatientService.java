package com.jedromz.doctorclinic.service;

import com.jedromz.doctorclinic.model.Patient;
import com.jedromz.doctorclinic.model.Visit;
import com.jedromz.doctorclinic.model.command.UpdatePatientCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Page<Patient> findAll(Pageable pageable);

    List<Patient> findAll();

    Optional<Patient> findById(Long id);

    Patient save(Patient patient);

    void deleteById(Long id);

    Patient edit(Patient toEdit, UpdatePatientCommand command);

    boolean existsByEmail(String email);


    Page<Visit> getPatientsVisits(long id, Pageable pageable);

}
