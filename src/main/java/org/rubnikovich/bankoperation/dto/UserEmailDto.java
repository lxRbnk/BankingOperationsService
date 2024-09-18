package org.rubnikovich.bankoperation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailDto {

    @Schema(hidden = true)
    private Long id;

    @Schema(description = "email", example = "some@mail.com")
    @NotEmpty(message = "email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(hidden = true)
    private Long userId;
}