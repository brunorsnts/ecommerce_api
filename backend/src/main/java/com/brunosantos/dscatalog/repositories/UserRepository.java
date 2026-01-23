package com.brunosantos.dscatalog.repositories;

import com.brunosantos.dscatalog.config.security.UserDetailsImpl;
import com.brunosantos.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
