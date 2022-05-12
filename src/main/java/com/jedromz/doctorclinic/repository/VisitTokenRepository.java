package com.jedromz.doctorclinic.repository;

import com.jedromz.doctorclinic.model.VisitToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface VisitTokenRepository extends JpaRepository<VisitToken, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<VisitToken> findByToken(String token);

    void deleteByToken(String token);
}
