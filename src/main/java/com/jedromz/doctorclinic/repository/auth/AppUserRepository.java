package com.jedromz.doctorclinic.repository.auth;

import com.jedromz.doctorclinic.model.auth.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    @Query(value = "select distinct u from AppUser u left join fetch u.roles",
            countQuery = "select count(u) from AppUser u")
    Page<AppUser> findAllWithRoles(Pageable pageable);
}
