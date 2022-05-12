package com.jedromz.doctorclinic.service.impl;

import com.jedromz.doctorclinic.error.EntityNotFoundException;
import com.jedromz.doctorclinic.model.Vacation;
import com.jedromz.doctorclinic.model.command.CreateVacationCommand;
import com.jedromz.doctorclinic.model.command.UpdateVacationCommand;
import com.jedromz.doctorclinic.repository.DoctorRepository;
import com.jedromz.doctorclinic.repository.VacationRepository;
import com.jedromz.doctorclinic.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public Page<Vacation> findAll(Pageable pageable) {
        return vacationRepository.findAll(pageable);
    }

    @Transactional
    public List<Vacation> findAll() {
        return vacationRepository.findAll();
    }

    @Transactional
    public Optional<Vacation> findById(Long id) {
        return Optional.empty();
    }

    @Transactional
    public Vacation save(Vacation vacation) {
        return vacationRepository.saveAndFlush(vacation);
    }

    @Transactional
    public void deleteById(Long id) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacation", Long.toString(id)));
        vacation.setDeleted(true);
    }

    @Transactional
    public Vacation edit(Vacation toEdit, UpdateVacationCommand command) {
        toEdit.setStart(command.getStart());
        toEdit.setEnd(command.getEnd());
        return toEdit;
    }

}
