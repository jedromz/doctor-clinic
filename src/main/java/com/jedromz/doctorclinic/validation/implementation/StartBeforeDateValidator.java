package com.jedromz.doctorclinic.validation.implementation;

import com.jedromz.doctorclinic.model.interfaces.IDateTimePeriod;
import com.jedromz.doctorclinic.validation.annotation.StartBeforeEndDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Service
@RequiredArgsConstructor
@Slf4j
public class StartBeforeDateValidator implements ConstraintValidator<StartBeforeEndDate, IDateTimePeriod> {

    @Override
    public boolean isValid(IDateTimePeriod iDateTimePeriod, ConstraintValidatorContext constraintValidatorContext) {

        return iDateTimePeriod.getStartDateTime().isBefore(iDateTimePeriod.getEndDateTime());
    }
}
