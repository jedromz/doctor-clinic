package com.jedromz.doctorclinic.model.interfaces;

import java.time.LocalTime;

public interface ITimePeriod {

    LocalTime getStartTime();

    LocalTime getEndTime();
}
