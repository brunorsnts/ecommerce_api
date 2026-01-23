package com.brunosantos.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.brunosantos.dscatalog.dto.UserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.brunosantos.dscatalog.entities.User;
import com.brunosantos.dscatalog.repositories.UserRepository;
import com.brunosantos.dscatalog.controller.exceptions.Error;


public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {
        List<Error> list = new ArrayList<>();
        Optional<User> user = repository.findByEmail(dto.getEmail());

        if (user.isPresent()) {
            list.add(new Error("email", "Email já existe"));
        }

        for (Error e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
