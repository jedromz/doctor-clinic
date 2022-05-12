package com.jedromz.doctorclinic.service;

import com.jedromz.doctorclinic.model.VisitToken;

import java.util.Optional;

public interface VisitTokenService {

    Optional<VisitToken> findByToken(String token);
}
