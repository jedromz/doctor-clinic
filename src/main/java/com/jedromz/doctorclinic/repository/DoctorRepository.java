package com.jedromz.doctorclinic.repository;

import com.jedromz.doctorclinic.model.Doctor;
import com.jedromz.doctorclinic.model.DoctorSpecialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from Doctor d left join fetch d.vacations left join fetch d.availabilities left join fetch d.visits where d.id = ?1")
    Optional<Doctor> findDoctorWithVisitsAndAvailabilitiesAndVacation(long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Doctor d WHERE (:firstname is null or d.firstname = :firstname) and (:lastname is null or d.lastname = :lastname) and (:nip is null or d.nip = :nip) and (:rate is null or d.rate = :rate)and (:specialization is null or d.specialization = :specialization) and (:isDeleted is null or d.deleted = :isDeleted)")
    Page<Doctor> findByParameters(Pageable pageable, String firstname, String lastname, String nip, BigDecimal rate,
                                  DoctorSpecialization specialization, boolean isDeleted);

    boolean existsByNip(String nip);
}
