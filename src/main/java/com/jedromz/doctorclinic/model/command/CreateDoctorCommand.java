package com.jedromz.doctorclinic.model.command;

import com.jedromz.doctorclinic.validation.annotation.UniqueNip;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDoctorCommand {

    @NotNull(message = "FIRSTNAME_NOT_NULL")
    @Pattern(regexp = "^[A-Za-z]*$", message = "FIRSTNAME_ONLY_LETTERS")
    private String firstname;
    @NotNull(message = "LASTNAME_NOT_NULL")
    @Pattern(regexp = "^[A-Za-z]*$", message = "LASTNAME_ONLY_LETTERS")
    private String lastname;
    @NotNull(message = "SPECIALIZATION_NOT_NULL")
    private String specialization;
    @NotNull(message = "RATE_NOT_NULL")
    private BigDecimal rate;
    @NotNull(message = "NIP_NOT_NULL")
    @UniqueNip
    private String nip;
}
