package com.jedromz.doctorclinic.service.impl;

import com.jedromz.doctorclinic.error.DateConflictException;
import com.jedromz.doctorclinic.error.EntityNotFoundException;
import com.jedromz.doctorclinic.model.Doctor;
import com.jedromz.doctorclinic.model.Patient;
import com.jedromz.doctorclinic.model.Visit;
import com.jedromz.doctorclinic.model.VisitToken;
import com.jedromz.doctorclinic.model.command.CreateVisitCommand;
import com.jedromz.doctorclinic.model.command.UpdateVisitCommand;
import com.jedromz.doctorclinic.model.dto.NotificationEmail;
import com.jedromz.doctorclinic.repository.DoctorRepository;
import com.jedromz.doctorclinic.repository.PatientRepository;
import com.jedromz.doctorclinic.repository.VisitRepository;
import com.jedromz.doctorclinic.repository.VisitTokenRepository;
import com.jedromz.doctorclinic.service.MailService;
import com.jedromz.doctorclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitTokenRepository visitTokenRepository;
    private final MailService mailService;

    @Transactional
    public Page<Visit> findAll(Pageable pageable) {
        return visitRepository.findAll(pageable);
    }

    @Transactional
    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    @Transactional
    public Optional<Visit> findById(Long id) {
        return visitRepository.findById(id);
    }

    @Transactional
    public Visit save(Visit visit) {
        return visitRepository.saveAndFlush(visit);
    }

    @Transactional
    public void confirmVisit(VisitToken visitToken) {
        long visitId = visitToken.getVisit().getId();
        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new EntityNotFoundException("Visit", Long.toString(visitId)));
        visit.setConfirmed(true);
    }

    @Transactional
    public void cancelVisit(VisitToken visitToken) {
        long visitId = visitToken.getVisit().getId();
        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new EntityNotFoundException("Visit", Long.toString(visitId)));
        visit.setCancelled(true);
    }

    @Transactional
    public void deleteById(Long id) {
        Visit visit = visitRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Visit", Long.toString(id)));
        visit.setDeleted(true);
    }

    @Transactional
    public Visit save(CreateVisitCommand command) throws DateConflictException {

        Doctor doctor = doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(command.getDoctorId()).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(command.getDoctorId())));
        Patient patient = patientRepository.findPatientWithVisits(command.getPatientId()).orElseThrow(() -> new EntityNotFoundException("Patient", Long.toString(command.getPatientId())));
        int visitTimeInMinutes = doctor.getAvailabilities()
                .stream()
                .filter(a -> a.getDayOfWeek().equals(command.getStartTime().getDayOfWeek()))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("Availability", command.getStartTime().getDayOfWeek().toString()))
                .getVisitDurationInMinutes();
        LocalDateTime visitStart = command.getStartTime();
        LocalDateTime visitEnd = visitStart.plusMinutes(visitTimeInMinutes);
        if (visitStart.isBefore(LocalDateTime.now())) {
            throw new DateConflictException(visitStart.toString(), "VISIT_DATE_BEFORE_NOW");
        }
        boolean visitTimeDoctorConflicting = isVisitTimeDoctorConflicting(visitStart, visitEnd, doctor);
        boolean isVisitPatientConflict = patient.getVisits().stream()
                .anyMatch(v -> visitStart.isAfter(v.getStart()) && visitStart.isBefore(v.getEnd()) || visitEnd.isAfter(v.getStart()) && visitEnd.isBefore(v.getEnd()));
        if (visitTimeDoctorConflicting) {
            throw new DateConflictException(visitStart.toString(), "DOCTOR_TIME_CONFLICT");
        }
        if (isVisitPatientConflict) {
            throw new DateConflictException(visitStart.toString(), "PATIENT_TIME_CONFLICT");
        }
        Visit visit = new Visit(visitStart, visitEnd);
        visit.setDoctor(doctor);
        visit.setPatient(patient);
        doctor.getVisits().add(visit);
        patient.getVisits().add(visit);
        VisitToken visitToken = generateVerificationToken(visit);
        mailService.sendMail(new NotificationEmail("doctor-clinic@info.com", "Visit confirmation", patient.getEmail(), "Thank you for signing up for a visit, please click the link below to confirm:" + "http://localhost:8080/api/visits/confirm/" + visitToken.getToken()
                + " your vet is: " + doctor.getFirstname() + " " + doctor.getLastname()));
        return visitRepository.save(visit);
    }

    private boolean isVisitTimeDoctorConflicting(LocalDateTime visitStart, LocalDateTime visitEnd, Doctor doctor) {
        //true if any visit exists in a conflicting time frame
        boolean isVisitDoctorConflict = doctor.getVisits().stream()
                .anyMatch(v -> visitStart.isAfter(v.getStart()) && visitStart.isBefore(v.getEnd()) || visitEnd.isAfter(v.getStart()) && visitEnd.isBefore(v.getEnd()));

        //true if doctor is available in a given day within a matching time frame
        boolean isDoctorAvailable = doctor.getAvailabilities()
                .stream()
                .filter(a -> a.getDayOfWeek().equals(visitStart.getDayOfWeek()))
                .anyMatch(a -> a.getStart().isBefore(visitStart.toLocalTime()) && a.getEnd().isBefore(visitEnd.toLocalTime()));
        //true if doctor is on vacation in a visits time
        boolean isDoctorOnVacation = doctor.getVacations()
                .stream()
                .anyMatch(v -> v.getStart().isBefore(visitStart.toLocalDate()) && v.getEnd().isBefore(visitStart.toLocalDate()));

        return isVisitDoctorConflict && !isDoctorAvailable && isDoctorOnVacation;
    }

    @Transactional
    public Visit edit(Visit toEdit, UpdateVisitCommand command) {
        toEdit.setStart(command.getStartDate());
        toEdit.setVersion(command.getVersion());
        return toEdit;
    }

    @Transactional
    public VisitToken generateVerificationToken(Visit visit) {
        String token = UUID.randomUUID().toString();
        VisitToken verificationToken = new VisitToken();
        verificationToken.setToken(token);
        verificationToken.setVisit(visit);
        return visitTokenRepository.saveAndFlush(verificationToken);
    }

    @Scheduled(cron = "00 00 23 * * *")
    public void nextDayVisitNotification() {
        List<Visit> tomorrowVisits = visitRepository.findAll().stream()
                .filter(v -> v.getStart().toLocalDate().equals(LocalDate.now().plusDays(1)))
                .filter(v -> v.isConfirmed() && !v.isCancelled())
                .toList();
        for (Visit tomorrowVisit : tomorrowVisits) {
            mailService.sendMail(new NotificationEmail("doctor-clinic@info.com", "Visit reminder", tomorrowVisit.getPatient().getEmail(), "Just a friendly reminder about your visit planned for tomorrow at: " + tomorrowVisit.getStart().toLocalTime()));
        }
    }

    @Transactional
    public boolean existsByEmail(String email) {
        return false;
    }

    @Transactional
    public void saveVacations(List<Visit> vacations) {

    }
}
