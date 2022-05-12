package com.jedromz.doctorclinic.service.impl;

import com.jedromz.doctorclinic.error.DateConflictException;
import com.jedromz.doctorclinic.error.EntityNotFoundException;
import com.jedromz.doctorclinic.model.Doctor;
import com.jedromz.doctorclinic.model.DoctorSpecialization;
import com.jedromz.doctorclinic.model.Vacation;
import com.jedromz.doctorclinic.model.command.CreateVacationCommand;
import com.jedromz.doctorclinic.model.command.UpdateDoctorCommand;
import com.jedromz.doctorclinic.repository.DoctorRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    private DoctorServiceImpl doctorService;
    @Mock
    private DoctorRepository doctorRepository;
    private List<Doctor> doctors;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Doctor DOC_1 = Doctor.builder().vacations(new HashSet<>()).build();
        Doctor DOC_2 = Doctor.builder().vacations(new HashSet<>()).build();
        Doctor DOC_3 = Doctor.builder().vacations(new HashSet<>()).build();
        doctors = new ArrayList<>(List.of(DOC_1, DOC_2, DOC_3));
        doctorService = new DoctorServiceImpl(doctorRepository);
    }

    @Test
    void findAll() {
        Page<Doctor> expected = new PageImpl<>(doctors);
        when(doctorRepository.findAll(any(Pageable.class))).thenReturn(expected);
        Page<Doctor> result = doctorService.findAll(Pageable.unpaged());
        verify(doctorRepository).findAll(Mockito.any(Pageable.class));
        assertEquals(expected, result);
    }

    @Test
    void findById() {
        Doctor expected = doctors.get(0);
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        Doctor result = doctorService.findById(anyLong()).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(anyLong())));
        Mockito.verify(doctorRepository).findById(anyLong());
        assertEquals(expected, result);
    }

    @Test
    void findByIdWithVisitsAndVacationAndAvailability() {
        Doctor expected = doctors.get(0);
        when(doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(anyLong())).thenReturn(Optional.of(expected));
        Doctor result = doctorService.findByIdWithVisitsAndVacationAndAvailability(anyLong()).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(Mockito.anyLong())));
        Mockito.verify(doctorRepository).findDoctorWithVisitsAndAvailabilitiesAndVacation(anyLong());
        assertEquals(expected, result);
    }

    @Test
    void save() {
        Doctor expected = doctors.get(0);
        Mockito.when(doctorRepository.saveAndFlush(expected))
                .thenReturn(expected);
        Doctor result = doctorService.save(expected);
        Mockito.verify(doctorRepository).saveAndFlush(expected);
        assertEquals(expected, result);
    }

    @Test
    void deleteById() {
        Doctor expected = doctors.get(0);
        when(doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(anyLong())).thenReturn(Optional.of(expected));
        doctorService.deleteById(anyLong());
        Mockito.verify(doctorRepository).findDoctorWithVisitsAndAvailabilitiesAndVacation(anyLong());
        assertTrue(expected.isDeleted());
    }

    @Test
    void edit() {
        Doctor expected = doctors.get(0);
        UpdateDoctorCommand command = UpdateDoctorCommand.builder()
                .firstname("TEST")
                .lastname("TEST")
                .nip("9291113345")
                .rate(new BigDecimal("340.00"))
                .specialization(DoctorSpecialization.CARDIOLOGIST)
                .version(0)
                .build();
        Mockito.when(doctorRepository.saveAndFlush(expected))
                .thenReturn(expected);
        Doctor result = doctorService.edit(expected, command);
        Mockito.verify(doctorRepository).saveAndFlush(expected);
        assertEquals(expected, result);
    }

    @Test
    @SneakyThrows
    void addVacation() {
        Doctor expected = doctors.get(0);
        when(doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(anyLong())).thenReturn(Optional.of(expected));
        Vacation vacation = Vacation.builder()
                .doctor(expected)
                .start(LocalDate.now().plusDays(10))
                .end(LocalDate.now().plusDays(20))
                .build();
        doctorService.addVacation(anyLong(), vacation);
        Mockito.verify(doctorRepository).findDoctorWithVisitsAndAvailabilitiesAndVacation(anyLong());
    }

    @Test
    @SneakyThrows
    void shouldThrowDateConflictException() {
        Doctor expected = doctors.get(0);
        when(doctorRepository.findDoctorWithVisitsAndAvailabilitiesAndVacation(anyLong())).thenReturn(Optional.of(expected));
        CreateVacationCommand command = CreateVacationCommand.builder()
                .start(LocalDate.now().plusDays(5))
                .end(LocalDate.now().plusDays(10))
                .build();
        expected.getVacations().add(Vacation.builder().start(LocalDate.now().plusDays(2)).end(LocalDate.now().plusDays(20)).build());
        assertThrows(
                DateConflictException.class,
                () -> doctorService.addVacation(anyLong(), command),
                "Expected addVacation() to throw, but it didn't"
        );
        Mockito.verify(doctorRepository).findDoctorWithVisitsAndAvailabilitiesAndVacation(anyLong());

    }

    @Test
    void existsByNip() {
        when(doctorRepository.existsByNip(anyString()))
                .thenReturn(true);
        boolean result = doctorService.existsByNip(anyString());
        Mockito.verify(doctorRepository).existsByNip(anyString());
        assertTrue(result);
    }
}
