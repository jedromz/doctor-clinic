package com.jedromz.doctorclinic.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddRoleToUserCommand {
    private String username;
    private String roleName;
}
