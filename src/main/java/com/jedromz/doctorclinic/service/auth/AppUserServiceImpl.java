package com.jedromz.doctorclinic.service.auth;

import com.jedromz.doctorclinic.model.auth.AppUser;
import com.jedromz.doctorclinic.model.auth.Role;
import com.jedromz.doctorclinic.repository.auth.AppUserRepository;
import com.jedromz.doctorclinic.repository.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AppUser save(AppUser user) {
        log.info("new user saved {}", user.getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public Role saveRole(Role role) {
        log.info("new role saved {}", role.getName());
        return roleRepository.saveAndFlush(role);
    }

    @Transactional
    public void addRoleToUser(String username, String roleName) {
        log.info("adding role {} to user {}", roleName, username);
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow();
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Transactional
    public AppUser getUser(String username) {
        log.info("retrieving user by username {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow();
    }

    @Transactional
    public Page<AppUser> findAll(Pageable pageable) {
        log.info("retrieving users page");
        return userRepository.findAllWithRoles(pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow();
        log.info("User found in the database {}", username);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
