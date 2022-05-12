package com.jedromz.doctorclinic.controller;

import com.jedromz.doctorclinic.error.DateConflictException;
import com.jedromz.doctorclinic.error.EntityNotFoundException;
import com.jedromz.doctorclinic.model.Vacation;
import com.jedromz.doctorclinic.model.Visit;
import com.jedromz.doctorclinic.model.command.CreateVacationCommand;
import com.jedromz.doctorclinic.model.command.CreateVisitCommand;
import com.jedromz.doctorclinic.model.dto.DoctorDto;
import com.jedromz.doctorclinic.model.dto.VacationDto;
import com.jedromz.doctorclinic.model.dto.VisitDto;
import com.jedromz.doctorclinic.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacations")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<Page<VacationDto>> getAllVacations(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(vacationService.findAll(pageable)
                .map(s -> modelMapper.map(s, VacationDto.class)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacationDto> getAllVacations(@PathVariable long id) {
        return vacationService.findById(id)
                .map(p -> modelMapper.map(p, VacationDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Vacation", Long.toString(id)));
    }

    @DeleteMapping()
    public ResponseEntity<Page<VacationDto>> deleteVacation(@PathVariable long id) {
        vacationService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
