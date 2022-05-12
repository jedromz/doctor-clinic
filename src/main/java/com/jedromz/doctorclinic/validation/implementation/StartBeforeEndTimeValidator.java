package com.jedromz.doctorclinic.validation.implementation;

import com.jedromz.doctorclinic.model.interfaces.ITimePeriod;
import com.jedromz.doctorclinic.validation.annotation.StartBeforeEndTime;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class StartBeforeEndTimeValidator implements ConstraintValidator<StartBeforeEndTime, ITimePeriod> {
    @Override
    public boolean isValid(ITimePeriod iTimePeriod, ConstraintValidatorContext constraintValidatorContext) {
        return iTimePeriod.getStartTime().isBefore(iTimePeriod.getEndTime());
    }
}
