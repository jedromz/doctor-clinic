package com.jedromz.doctorclinic.model.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVisitCommand {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "START_TIME_NOT_NULL")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "DOCTOR_ID_NOT_NULL")
    private Long doctorId;
    @NotNull(message = "PATIENT_ID_NOT_NULL")
    private Long patientId;
}
