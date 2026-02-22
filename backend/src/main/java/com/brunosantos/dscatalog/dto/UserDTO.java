package com.brunosantos.dscatalog.dto;

import com.brunosantos.dscatalog.entities.Role;
import com.brunosantos.dscatalog.entities.User;
import com.brunosantos.dscatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UserInsertValid
public class UserDTO {

    private Long id;
    @NotBlank(message = "Campo obrigatório")
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    private Set<Role> roles = new HashSet<>();

    public UserDTO(User entity) {
        this.id = entity.getUserId();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.roles = getRoles();
        roles.addAll(entity.getRoles());
    }

}
