package com.jedromz.doctorclinic.service.auth;

import com.jedromz.doctorclinic.model.auth.AppUser;
import com.jedromz.doctorclinic.model.auth.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AppUserService {

    AppUser save(AppUser user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    AppUser getUser(String userName);

    Page<AppUser> findAll(Pageable pageable);

}
