package com.brunosantos.dscatalog.services;

import com.brunosantos.dscatalog.dto.*;
import com.brunosantos.dscatalog.entities.Role;
import com.brunosantos.dscatalog.entities.User;
import com.brunosantos.dscatalog.repositories.UserRepository;
import com.brunosantos.dscatalog.services.exceptions.CreateUserException;
import com.brunosantos.dscatalog.services.exceptions.DatabaseException;
import com.brunosantos.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.Long;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
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
        User entity = null;
        if (!usuarioJaCadastrado(dto)) {
            entity = new User(dto);
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            entity.getRoles().add(Role.BASIC);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } else {
            throw new CreateUserException("Usuário já está cadastrado no sistema, entre com o login e senha");
        }
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
//            CopyDtoToEntity(dto, entity);
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

//    private void CopyDtoToEntity(UserDTO dto, User entity) {
//        entity.setEmail(dto.getEmail());
//        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
//        entity.setUserId(dto.getId());
//        if (!usuarioJaCadastrado(dto)) {
//            Role role = roleRepository.findByName(Role.Values.BASIC.name());
//            entity.setRoles(Set.of(role));
//        } else {
//            Role role = roleRepository.find
//        }
//    }

    private boolean usuarioJaCadastrado(UserDTO dto) {
        try {
            User user = repository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Usuário ainda não cadastrado"));
        } catch (ResourceNotFoundException e) {
            log.error("Cadastrando Usuário no sistema");
            return false;
        }
        return true;
    }

}
