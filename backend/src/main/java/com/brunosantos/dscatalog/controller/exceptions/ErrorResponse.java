package com.brunosantos.dscatalog.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ErrorResponse {

    private List<Error> errors = new ArrayList<>();

    public void addError(Error error) {
        this.errors.add(error);
    }
}
