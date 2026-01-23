package com.brunosantos.dscatalog.services.validation;

import com.brunosantos.dscatalog.dto.UserUpdateDTO;
import com.brunosantos.dscatalog.entities.User;
import com.brunosantos.dscatalog.repositories.UserRepository;
import com.brunosantos.dscatalog.controller.exceptions.Error;
import com.brunosantos.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private UserRepository repository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        var userId = Long.parseLong(uriVars.get("id"));

        List<Error> list = new ArrayList<>();

        User user = repository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + dto.getEmail()));

        if (user != null && userId != user.getUserId()) {
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
