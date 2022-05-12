package com.jedromz.doctorclinic.error.constraint.implementation;

import com.jedromz.doctorclinic.error.constraint.ConstraintErrorHandler;

public class PatientEmailUniqueConstraintHandler implements ConstraintErrorHandler {
    @Override
    public String constraintName() {
        return "UC_PATIENT_EMAIL";
    }

    @Override
    public String message() {
        return "EMAIL_NOT_UNIQUE";
    }

    @Override
    public String field() {
        return "patient_email";
    }
}
