package com.jedromz.doctorclinic.service;

import com.jedromz.doctorclinic.error.DateConflictException;
import com.jedromz.doctorclinic.model.Visit;
import com.jedromz.doctorclinic.model.VisitToken;
import com.jedromz.doctorclinic.model.command.CreateVisitCommand;
import com.jedromz.doctorclinic.model.command.UpdateVisitCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VisitService {
    Page<Visit> findAll(Pageable pageable);

    List<Visit> findAll();

    Optional<Visit> findById(Long id);

    Visit save(CreateVisitCommand visit) throws DateConflictException;

    void deleteById(Long id);

    Visit edit(Visit toEdit, UpdateVisitCommand command);

    boolean existsByEmail(String email);

    void saveVacations(List<Visit> vacations);

    Visit save(Visit visit);

    void confirmVisit(VisitToken visitToken);

    void cancelVisit(VisitToken visitToken);
}
