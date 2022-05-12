package com.jedromz.doctorclinic.model.interfaces;

import java.time.LocalDateTime;

public interface IDateTimePeriod {

    LocalDateTime getStartDateTime();
    LocalDateTime getEndDateTime();

}
