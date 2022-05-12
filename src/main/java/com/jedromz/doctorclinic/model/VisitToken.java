package com.jedromz.doctorclinic.model;


import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "visit_token")
public class VisitToken {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String token;
    @OneToOne
    private Visit visit;
    @Version
    private int version;

    public VisitToken(String token, Visit visit) {
        this.token = token;
        this.visit = visit;
    }
}
