package com.jedromz.doctorclinic.controller;

import com.jedromz.doctorclinic.error.EntityNotFoundException;
import com.jedromz.doctorclinic.model.Availability;
import com.jedromz.doctorclinic.model.Doctor;
import com.jedromz.doctorclinic.model.DoctorSpecialization;
import com.jedromz.doctorclinic.model.Vacation;
import com.jedromz.doctorclinic.model.command.CreateAvailabilityCommand;
import com.jedromz.doctorclinic.model.command.CreateDoctorCommand;
import com.jedromz.doctorclinic.model.command.CreateVacationCommand;
import com.jedromz.doctorclinic.model.command.UpdateDoctorCommand;
import com.jedromz.doctorclinic.model.dto.AvailabilityDto;
import com.jedromz.doctorclinic.model.dto.DoctorDto;
import com.jedromz.doctorclinic.model.dto.VacationDto;
import com.jedromz.doctorclinic.model.dto.VisitDto;
import com.jedromz.doctorclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
@Slf4j
public class DoctorController {

    private final ModelMapper modelMapper;
    private final DoctorService doctorService;

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctor(@PathVariable long id) {
        return doctorService.findByIdWithVisitsAndVacationAndAvailability(id)
                .map(p -> modelMapper.map(p, DoctorDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
    }

    @GetMapping()
    public ResponseEntity<Page<DoctorDto>> getAllDoctors(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(doctorService.findAll(pageable)
                .map(s -> modelMapper.map(s, DoctorDto.class)));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DoctorDto>> searchDoctors(@RequestParam Map<String, String> searchParams, @PageableDefault Pageable pageable) {
        String firstname = searchParams.get("firstname");
        String lastname = searchParams.get("lastname");
        String nip = searchParams.get("nip");
        boolean deleted = Boolean.parseBoolean(searchParams.get("deleted"));
        String rateParam = searchParams.get("rate");
        String specializationParam = searchParams.get("specialization");
        BigDecimal rate = null;
        DoctorSpecialization specialization = null;
        if (!(rateParam == null)) {
            rate = new BigDecimal(rateParam);
        }
        if (!(specializationParam == null)) {
            specialization = DoctorSpecialization.valueOf(specializationParam);
        }

        return ResponseEntity.ok(doctorService.findByParameters(pageable, firstname, lastname, nip, rate, specialization, deleted)
                .map(s -> modelMapper.map(s, DoctorDto.class)));
    }

    @PostMapping()
    public ResponseEntity<DoctorDto> saveDoctor(@RequestBody @Valid CreateDoctorCommand command) {
        Doctor doctor = doctorService.save(modelMapper.map(command, Doctor.class));
        return new ResponseEntity(modelMapper.map(doctor, DoctorDto.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteVet(@PathVariable long id) {
        doctorService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> editDoctor(@PathVariable long id, @RequestBody @Valid UpdateDoctorCommand command) {
        Doctor toEdit = doctorService.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor", Long.toString(id)));
        Doctor edited = doctorService.edit(toEdit, command);
        return new ResponseEntity(modelMapper.map(edited, DoctorDto.class), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/visits")
    public ResponseEntity<Page<VisitDto>> getDoctorsVisits(@PathVariable long id, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(doctorService.getDoctorsVisits(id, pageable)
                .map(v -> modelMapper.map(v, VisitDto.class)));
    }

    @GetMapping("/{id}/vacations")
    public ResponseEntity<Page<VacationDto>> getDoctorsVacations(@PathVariable long id, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(doctorService.getDoctorsVacations(id, pageable)
                .map(v -> modelMapper.map(v, VacationDto.class)));
    }

    @PostMapping("/{id}/vacations")
    @SneakyThrows
    public ResponseEntity<VacationDto> addVacation(@PathVariable long id, @RequestBody @Valid CreateVacationCommand command) {
        Vacation vacation = doctorService.addVacation(id, command);
        return new ResponseEntity(modelMapper.map(vacation, VacationDto.class), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/availabilities")
    public ResponseEntity<Page<AvailabilityDto>> getDoctorsAvailabilities(@PathVariable long id, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(doctorService.getDoctorsAvailabilities(id, pageable)
                .map(a -> modelMapper.map(a, AvailabilityDto.class)));
    }

    @PostMapping("/{id}/availabilities")
    @SneakyThrows
    public ResponseEntity<AvailabilityDto> addAvailability(@PathVariable long id, @RequestBody @Valid CreateAvailabilityCommand command) {
        Availability availability = doctorService.addAvailability(id, command);
        return new ResponseEntity(modelMapper.map(availability, AvailabilityDto.class), HttpStatus.CREATED);
    }

}
