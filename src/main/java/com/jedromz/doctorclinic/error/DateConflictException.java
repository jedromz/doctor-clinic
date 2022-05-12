package com.jedromz.doctorclinic.error;

import lombok.Value;

@Value
public class DateConflictException extends Exception {
    private String date;
    private String message;
}
