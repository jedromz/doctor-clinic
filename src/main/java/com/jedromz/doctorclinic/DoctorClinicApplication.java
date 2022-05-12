package com.jedromz.doctorclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DoctorClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoctorClinicApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Lekarz(id, imie, nazwisko, specjalizacja), Pacjent(id, imie, nazwisko, wiek), Wizyta(data, lekarz, pacjenta, schorzenie)
    //CRUD: Lekarzy, Pacjentow (uwaga: delete ma byc na zasadzie "soft delete") active=true/false
    //Tworzenie wizyt: walidacje (...) kazda wizyta trwa 30 minut* i wizyty nie moga sie nakladac na lekarzy i pacjentow
    //to znaczy: learz A ma wizyte w srode o 16:30, z pacjentem B,
    //pacjent C chce stworzyc wizyte do lekarza A do 16:45 to sie NIE da bo ma juz jakas tam wizyte.
    //pesymistic lock, optymistic lock
    //Kazdy doktor ma swoj grafik.
    //grafik definuje w jakich godzinach w danym dniu tygodnia doktor przymuje oraz ile czasu w danym dniu trwa wizyta.
    //dodatkowo grafik powinien uwzgledniac urlopy lekarza.

    //czyli: podczas tworzenia wizyty nalezy sprawdzic czy godziny przyjmowania w dany dzien pokrywaja sie z obecnoscia danego doktora
    //i czy nie ma urlopu w danym czasie.
    //Rest api DO zarzadzania grafikiem.

}
