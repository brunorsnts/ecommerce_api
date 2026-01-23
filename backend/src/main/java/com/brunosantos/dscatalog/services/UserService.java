package com.brunosantos.dscatalog.services;

import com.brunosantos.dscatalog.dto.*;
import com.brunosantos.dscatalog.entities.Role;
import com.brunosantos.dscatalog.entities.User;
import com.brunosantos.dscatalog.repositories.RoleRepository;
import com.brunosantos.dscatalog.repositories.UserRepository;
import com.brunosantos.dscatalog.services.exceptions.DatabaseException;
import com.brunosantos.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.Long;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> list = repository.findAll(pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(entity);

    }

    @Transactional
    public UserDTO registerUser(UserDTO dto) {
        User entity = new User();
        CopyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
            CopyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + email));
        return new UserDTO(user);
    }

    private void CopyDtoToEntity(UserDTO dto, User entity) {
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setUserId(dto.getId());
        Role role = roleRepository.findByName(Role.Values.BASIC.name());
        entity.setRoles(Set.of(role));
    }

}
