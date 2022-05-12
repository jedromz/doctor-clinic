package com.jedromz.doctorclinic.validation.implementation;

import com.jedromz.doctorclinic.service.DoctorService;
import com.jedromz.doctorclinic.validation.annotation.UniqueNip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
@RequiredArgsConstructor
public class UniqueNipValidator implements ConstraintValidator<UniqueNip, String> {

    private final DoctorService doctorService;

    @Override
    public boolean isValid(String nip, ConstraintValidatorContext constraintValidatorContext) {
        return !doctorService.existsByNip(nip);
    }
}
