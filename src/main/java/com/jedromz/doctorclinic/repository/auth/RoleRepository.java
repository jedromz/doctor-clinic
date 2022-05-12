package com.jedromz.doctorclinic.repository.auth;

import com.jedromz.doctorclinic.model.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String role);
}
