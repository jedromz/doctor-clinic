package com.jedromz.doctorclinic.service;

import com.jedromz.doctorclinic.model.Vacation;
import com.jedromz.doctorclinic.model.command.CreateVacationCommand;
import com.jedromz.doctorclinic.model.command.UpdateVacationCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VacationService {
    Page<Vacation> findAll(Pageable pageable);

    List<Vacation> findAll();

    Optional<Vacation> findById(Long id);

    Vacation save(Vacation vacation);

    void deleteById(Long id);

    Vacation edit(Vacation toEdit, UpdateVacationCommand command);

}
