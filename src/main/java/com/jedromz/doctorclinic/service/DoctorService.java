package com.jedromz.doctorclinic.service;

import com.jedromz.doctorclinic.error.DateConflictException;
import com.jedromz.doctorclinic.model.*;
import com.jedromz.doctorclinic.model.command.CreateAvailabilityCommand;
import com.jedromz.doctorclinic.model.command.CreateVacationCommand;
import com.jedromz.doctorclinic.model.command.UpdateDoctorCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Page<Doctor> findAll(Pageable pageable);

    List<Doctor> findAll();

    Optional<Doctor> findById(Long id);

    Optional<Doctor> findByIdWithVisitsAndVacationAndAvailability(Long id);

    Page<Doctor> findByParameters(Pageable pageable, String firstname, String lastname, String nip, BigDecimal rate,
                                  DoctorSpecialization doctorSpecialization, boolean isDeleted);

    Doctor save(Doctor doctor);

    void deleteById(Long id);

    Doctor edit(Doctor toEdit, UpdateDoctorCommand command);

    boolean existsByEmail(String email);

    public Vacation addVacation(Long id, Vacation vacation) throws DateConflictException;

    public Vacation addVacation(Long id, CreateVacationCommand command) throws DateConflictException;

    public Availability addAvailability(Long id, Availability availability) throws DateConflictException;

    public Availability addAvailability(Long id, CreateAvailabilityCommand command) throws DateConflictException;

    Page<Visit> getDoctorsVisits(long id, Pageable pageable);

    Page<Vacation> getDoctorsVacations(long id, Pageable pageable);

    Page<Availability> getDoctorsAvailabilities(long id, Pageable pageable);

    boolean existsByNip(String nip);

    void deleteVacation(long id);
}
