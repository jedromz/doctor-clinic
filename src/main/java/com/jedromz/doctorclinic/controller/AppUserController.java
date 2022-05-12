package com.jedromz.doctorclinic.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jedromz.doctorclinic.jwt.JwtService;
import com.jedromz.doctorclinic.model.auth.AppUser;
import com.jedromz.doctorclinic.model.auth.Role;
import com.jedromz.doctorclinic.model.command.AddRoleToUserCommand;
import com.jedromz.doctorclinic.service.auth.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;
    private final JwtService jwtService;


    @GetMapping("/users")
    public ResponseEntity<Page<AppUser>> getAllStudents(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @PostMapping("/users")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser user) {
        AppUser savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        Role savedRole = userService.saveRole(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @PostMapping("/user/roles")
    public ResponseEntity<?> addRoleToUser(@RequestBody AddRoleToUserCommand command) {
        userService.addRoleToUser(command.getUsername(), command.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                JWTVerifier verifier = JWT.require(jwtService.getJwtConfig().algorithm()).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getUser(username);
                String access_token = jwtService.generateAccessToken(request, user);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}

