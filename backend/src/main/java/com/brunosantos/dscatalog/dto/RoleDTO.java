package com.brunosantos.dscatalog.dto;

import com.brunosantos.dscatalog.entities.Role;

import java.util.Set;
import java.util.UUID;

public class RoleDTO {

    public Long roleId;
    public String name;

    public RoleDTO(Role role) {
        this.roleId = role.getRoleId();
        this.name = role.getName();
    }
}
