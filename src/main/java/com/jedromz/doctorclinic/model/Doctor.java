package com.jedromz.doctorclinic.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"nip"}, name = "UC_DOCTOR_NIP")})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String nip;
    private BigDecimal rate;
    @Enumerated(EnumType.STRING)
    private DoctorSpecialization specialization;
    private boolean deleted;
    @Version
    private int version;
    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.ALL})
    private Set<Visit> visits = new HashSet<>();
    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.ALL})
    private Set<Availability> availabilities = new HashSet<>();
    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.ALL})
    private Set<Vacation> vacations = new HashSet<>();
}
