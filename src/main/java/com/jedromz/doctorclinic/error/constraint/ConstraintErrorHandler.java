package com.jedromz.doctorclinic.error.constraint;

public interface ConstraintErrorHandler {
    String constraintName();

    String message();

    String field();
}
