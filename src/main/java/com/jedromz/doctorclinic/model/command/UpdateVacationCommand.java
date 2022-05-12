package com.jedromz.doctorclinic.model.command;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVacationCommand {
    private LocalDate start;
    private LocalDate end;
}
