package com.jedromz.doctorclinic.security;

import com.jedromz.doctorclinic.filter.JwtTokenVerifier;
import com.jedromz.doctorclinic.filter.JwtUsernamePasswordAuthenticationFilter;
import com.jedromz.doctorclinic.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtUsernamePasswordAuthenticationFilter customAuthenticationFilter = new JwtUsernamePasswordAuthenticationFilter(authenticationManagerBean(), jwtService);
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/auth/login/**", "/auth/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers(GET, "/auth/user/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/auth/patients/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT");
        http.authorizeRequests().antMatchers(POST, "/auth/patients/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR");
        http.authorizeRequests().antMatchers(GET, "/auth/doctors/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR");
        http.authorizeRequests().antMatchers(POST, "/auth/doctors/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR");
        http.authorizeRequests().antMatchers(POST, "/auth/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new JwtTokenVerifier(jwtService.getJwtConfig().algorithm()), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
