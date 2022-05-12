package com.jedromz.doctorclinic.service.impl;

import com.jedromz.doctorclinic.error.EntityNotFoundException;
import com.jedromz.doctorclinic.model.Patient;
import com.jedromz.doctorclinic.model.Visit;
import com.jedromz.doctorclinic.model.command.UpdatePatientCommand;
import com.jedromz.doctorclinic.repository.PatientRepository;
import com.jedromz.doctorclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Transactional
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Transactional
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Transactional
    public Patient save(Patient patient) {
        return patientRepository.saveAndFlush(patient);
    }

    @Transactional
    public void deleteById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient", String.valueOf(id)));
        patient.setDeleted(true);
    }

    @Transactional
    public Patient edit(Patient toEdit, UpdatePatientCommand command) {
        toEdit.setFirstname(command.getFirstname());
        toEdit.setLastname(command.getLastname());
        toEdit.setBirthdate(command.getBirthdate());
        toEdit.setDeleted(command.isDeleted());
        toEdit.setVersion(command.getVersion());
        return toEdit;
    }

    @Transactional
    public boolean existsByEmail(String email) {
        return patientRepository.existsByEmail(email);
    }

    @Transactional
    public Page<Visit> getPatientsVisits(long id, Pageable pageable) {
        Patient patient = patientRepository.findPatientWithVisits(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient", Long.toString(id)));
        return new PageImpl<>(List.copyOf(patient.getVisits()), pageable, patient.getVisits().size());
    }

}
