package com.jedromz.doctorclinic.model.command;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVisitCommand {
    @NotNull(message = "START_DATE_NOT_NULL")
    private LocalDateTime startDate;
    @NotNull(message = "VERSION_NOT_NULL")
    private int version;
}
