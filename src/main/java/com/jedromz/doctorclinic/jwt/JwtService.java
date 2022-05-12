package com.jedromz.doctorclinic.jwt;

import com.auth0.jwt.JWT;
import com.jedromz.doctorclinic.model.auth.AppUser;
import com.jedromz.doctorclinic.model.auth.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateAccessToken(HttpServletRequest request, User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getTokenExpirationAfterMillis()))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(jwtConfig.algorithm());
    }

    public String generateRefreshToken(HttpServletRequest request, User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getTokenExpirationAfterMillis()))
                .withIssuer(request.getRequestURL().toString())
                .sign(jwtConfig.algorithm());
    }

    public String generateAccessToken(HttpServletRequest request, AppUser user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getTokenExpirationAfterMillis()))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(jwtConfig.algorithm());
    }
}
