package com.brunosantos.dscatalog.dto;

import com.brunosantos.dscatalog.services.validation.UserInsertValid;
import com.brunosantos.dscatalog.services.validation.UserUpdateValid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@UserUpdateValid
public class UserUpdateDTO extends UserDTO {
}
