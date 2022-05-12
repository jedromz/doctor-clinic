package com.jedromz.doctorclinic.controller;

import com.jedromz.doctorclinic.error.EntityNotFoundException;
import com.jedromz.doctorclinic.model.Patient;
import com.jedromz.doctorclinic.model.command.CreatePatientCommand;
import com.jedromz.doctorclinic.model.command.UpdatePatientCommand;
import com.jedromz.doctorclinic.model.dto.DoctorDto;
import com.jedromz.doctorclinic.model.dto.PatientDto;
import com.jedromz.doctorclinic.model.dto.VisitDto;
import com.jedromz.doctorclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable long id) {
        return patientService.findById(id)
                .map(p -> modelMapper.map(p, PatientDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Patient", Long.toString(id)));
    }

    @GetMapping()
    public ResponseEntity<Page<PatientDto>> getAllPatients(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(patientService.findAll(pageable)
                .map(s -> modelMapper.map(s, PatientDto.class)));
    }

    @PostMapping()
    public ResponseEntity<DoctorDto> savePatient(@RequestBody @Valid CreatePatientCommand command) {
        Patient patient = patientService.save(modelMapper.map(command, Patient.class));
        return new ResponseEntity(modelMapper.map(patient, PatientDto.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePatient(@PathVariable long id) {
        patientService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> editPatient(@PathVariable long id, @RequestBody @Valid UpdatePatientCommand command) {
        Patient toEdit = patientService.findById(id).orElseThrow(() -> new EntityNotFoundException("Vet", Long.toString(id)));
        Patient edited = patientService.edit(toEdit, command);
        return new ResponseEntity(modelMapper.map(edited, DoctorDto.class), HttpStatus.OK);
    }

    @GetMapping("/{id}/visits")
    public ResponseEntity<Page<VisitDto>> getPatientsVisits(@PathVariable long id, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(patientService.getPatientsVisits(id, pageable)
                .map(v -> modelMapper.map(v, VisitDto.class)));
    }
}
