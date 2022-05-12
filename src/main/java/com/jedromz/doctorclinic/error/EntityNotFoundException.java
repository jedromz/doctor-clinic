package com.jedromz.doctorclinic.error;

import lombok.Value;

@Value
public class EntityNotFoundException extends RuntimeException {
    private String name;
    private String key;
}
