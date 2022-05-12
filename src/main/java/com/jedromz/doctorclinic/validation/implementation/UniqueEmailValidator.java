package com.jedromz.doctorclinic.validation.implementation;

import com.jedromz.doctorclinic.service.PatientService;
import com.jedromz.doctorclinic.validation.annotation.UniqueEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final PatientService patientService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !patientService.existsByEmail(email);
    }
}
