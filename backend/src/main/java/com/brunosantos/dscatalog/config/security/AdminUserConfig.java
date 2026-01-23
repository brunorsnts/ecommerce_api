package com.brunosantos.dscatalog.config.security;

import com.brunosantos.dscatalog.entities.Role;
import com.brunosantos.dscatalog.entities.User;
import com.brunosantos.dscatalog.repositories.RoleRepository;
import com.brunosantos.dscatalog.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Role roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByEmail("admin@gmail.com");

        userAdmin.ifPresentOrElse(
                users -> {
                    System.out.println("admin já existe");
                    },
                    () -> {
                    User user = new User();
                    user.setEmail("admin@gmail.com");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                });
    }
}
