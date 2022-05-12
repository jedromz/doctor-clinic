package com.jedromz.doctorclinic.model.command;

import com.jedromz.doctorclinic.validation.annotation.UniqueEmail;
import com.jedromz.doctorclinic.validation.annotation.UniqueNip;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePatientCommand {
    @NotNull(message = "FIRSTNAME_NOT_NULL")
    @Pattern(regexp = "^[A-Za-z]*$", message = "FIRSTNAME_ONLY_LETTERS")
    private String firstname;
    @NotNull(message = "LASTNAME_NOT_NULL")
    @Pattern(regexp = "^[A-Za-z]*$", message = "LASTNAME_ONLY_LETTERS")
    private String lastname;
    @NotNull(message = "BIRTHDATE_NOT_NULL")
    private LocalDate birthdate;
    @Email
    @NotNull(message = "EMAIL_NOT_NULL")
    @UniqueEmail
    private String email;
    private boolean isDeleted;
    @NotNull(message = "VERSION_NOT_NULL")
    private Integer version;
}
