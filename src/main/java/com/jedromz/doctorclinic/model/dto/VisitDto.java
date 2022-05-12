package com.jedromz.doctorclinic.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitDto extends RepresentationModel<VisitDto> {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean confirmed;
    private boolean cancelled;
    private Integer version;
}
