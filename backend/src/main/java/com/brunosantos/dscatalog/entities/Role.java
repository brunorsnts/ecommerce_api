package com.brunosantos.dscatalog.entities;

import com.brunosantos.dscatalog.dto.RoleDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;

    public Role(RoleDTO roleDTO) {
        Role role = new Role(roleDTO);
    }

    @Getter
    public enum Values {
        ADMIN(1L),
        BASIC(2L);


        long roleId;

        Values(long roleId) {
            this.roleId = roleId;
        }
    }
}
