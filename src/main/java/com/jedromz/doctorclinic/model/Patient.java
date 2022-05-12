package com.jedromz.doctorclinic.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}, name = "UC_PATIENT_EMAIL")})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    @Past
    private LocalDate birthdate;
    @Email
    private String email;
    private boolean deleted;
    @Version
    private int version;
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.ALL})
    private Set<Visit> visits = new HashSet<>();
}
