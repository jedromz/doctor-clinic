package com.jedromz.doctorclinic.model.command;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVacationCommand {
    private LocalDate start;
    private LocalDate end;
    private Long doctorId;
}
