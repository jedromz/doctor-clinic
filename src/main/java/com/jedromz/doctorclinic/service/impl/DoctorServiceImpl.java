package com.jedromz.doctorclinic.service.impl;

import com.jedromz.doctorclinic.error.DateConflictException;
import com.jedromz.doctorclinic.error.EntityNotFoundException;
import com.jedromz.doctorclinic.model.*;
import com.jedromz.doctorclinic.model.command.CreateAvailabilityCommand;
import com.jedromz.doctorclinic.model.command.CreateVacationCommand;
import com.jedromz.doctorclinic.model.command.UpdateDoctorCommand;
import com.jedromz.doctorclinic.repository.DoctorRepository;
import com.jedromz.doctorclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Transactional
    public Page<Doctor> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    @Transactional
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Transactional
    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }


    @Transactional
    public Optional<Doctor> findByIdWithVisitsAndVacationAndAvailability(Long id) {
        return doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(id);
    }

    @Transactional
    public Page<Doctor> findByParameters(Pageable pageable, String firstname, String lastname, String nip, BigDecimal rate, DoctorSpecialization doctorSpecialization, boolean isDeleted) {
        return doctorRepository.findByParameters(pageable, firstname, lastname, nip, rate, doctorSpecialization, isDeleted);
    }

    @Transactional
    public Doctor save(Doctor doctor) {
        Doctor savedDoctor = doctorRepository.saveAndFlush(doctor);
        return savedDoctor;
    }

    @Transactional
    public void deleteById(Long id) {
        Doctor doctor = doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        doctor.setDeleted(true);
    }

    @Transactional
    public Doctor edit(Doctor toEdit, UpdateDoctorCommand command) {
        toEdit.setFirstname(command.getFirstname());
        toEdit.setLastname(command.getLastname());
        toEdit.setRate(command.getRate());
        toEdit.setNip(command.getNip());
        toEdit.setSpecialization(command.getSpecialization());
        toEdit.setVersion(command.getVersion());
        return doctorRepository.saveAndFlush(toEdit);
    }

    @Transactional
    public boolean existsByEmail(String email) {
        return false;
    }

    @Transactional
    public Vacation addVacation(Long id, Vacation vacation) throws DateConflictException {
        Doctor doctor = findByIdWithVisitsAndVacationAndAvailability(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        boolean vacationConflict = doctor.getVacations().stream().anyMatch(v -> v.getStart().isBefore(vacation.getStart()) && v.getEnd().isAfter(vacation.getEnd()));
        if (vacationConflict) {
            throw new DateConflictException(vacation.getStart().toString(), "VACATION_CONFLICT");
        }
        doctor.getVacations().add(vacation);
        vacation.setDoctor(doctor);
        return vacation;
    }

    @Transactional
    public Vacation addVacation(Long id, CreateVacationCommand command) throws DateConflictException {
        Doctor doctor = findByIdWithVisitsAndVacationAndAvailability(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        boolean vacationConflict = doctor.getVacations().stream().anyMatch(v -> v.getStart().isBefore(command.getStart()) && v.getEnd().isAfter(command.getEnd()));
        if (vacationConflict) {
            throw new DateConflictException(command.getStart().toString(), "VACATION_CONFLICT");
        }
        Vacation vacation = Vacation.builder().start(command.getStart()).end(command.getEnd()).doctor(doctor).build();
        doctor.getVacations().add(vacation);
        return vacation;
    }

    @Transactional
    public Availability addAvailability(Long id, Availability availability) throws DateConflictException {
        Doctor doctor = doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        boolean availabilityConflict = doctor.getAvailabilities().stream().anyMatch(a -> (a.getStart()
                .isBefore(availability.getStart()) && a.getEnd().isAfter(availability.getEnd())) || a.getDayOfWeek().equals(availability.getDayOfWeek()));
        if (availabilityConflict) {
            throw new DateConflictException(availability.getStart().toString(), "AVAILABILITY_CONFLICT");
        }
        doctor.getAvailabilities().add(availability);
        availability.setDoctor(doctor);
        return availability;
    }

    @Transactional
    public Availability addAvailability(Long id, CreateAvailabilityCommand command) throws DateConflictException {
        Doctor doctor = doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        boolean availabilityConflict = doctor.getAvailabilities().stream().anyMatch(v -> v.getStart().isBefore(command.getStart()) && v.getEnd().isAfter(command.getEnd()));
        if (availabilityConflict) {
            throw new DateConflictException(command.getStart().toString(), "VACATION_CONFLICT");
        }
        Availability availability = Availability.builder().dayOfWeek(command.getDayOfWeek()).start(command.getStart()).end(command.getEnd()).build();
        doctor.getAvailabilities().add(availability);
        availability.setDoctor(doctor);
        return availability;
    }


    @Transactional
    public Page<Availability> getDoctorsAvailabilities(long id, Pageable pageable) {
        Doctor doctor = doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        return new PageImpl<>(List.copyOf(doctor.getAvailabilities()), pageable, doctor.getAvailabilities().size());
    }

    @Override
    public boolean existsByNip(String nip) {
        return doctorRepository.existsByNip(nip);
    }

    @Override
    public void deleteVacation(long id) {

    }

    @Transactional
    public Page<Visit> getDoctorsVisits(long id, Pageable pageable) {
        Doctor doctor = doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        return new PageImpl<>(List.copyOf(doctor.getVisits()), pageable, doctor.getVisits().size());
    }

    @Transactional
    public Page<Vacation> getDoctorsVacations(long id, Pageable pageable) {
        Doctor doctor = doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        return new PageImpl<>(List.copyOf(doctor.getVacations()), pageable, doctor.getVisits().size());
    }

}
