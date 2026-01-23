package com.brunosantos.dscatalog.repositories;

import com.brunosantos.dscatalog.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String nome);
}
