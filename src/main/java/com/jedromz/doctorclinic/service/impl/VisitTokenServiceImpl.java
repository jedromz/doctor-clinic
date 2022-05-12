package com.jedromz.doctorclinic.service.impl;

import com.jedromz.doctorclinic.model.VisitToken;
import com.jedromz.doctorclinic.repository.VisitTokenRepository;
import com.jedromz.doctorclinic.service.VisitTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitTokenServiceImpl implements VisitTokenService {

    private final VisitTokenRepository visitTokenRepository;

    @Transactional
    public Optional<VisitToken> findByToken(String token) {
        return visitTokenRepository.findByToken(token);
    }
}
