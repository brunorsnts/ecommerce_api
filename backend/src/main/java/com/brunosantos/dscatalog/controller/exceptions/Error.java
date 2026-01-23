package com.brunosantos.dscatalog.controller.exceptions;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {

    private String fieldName;
    private String message;
}
