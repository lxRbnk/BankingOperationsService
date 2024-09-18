package org.rubnikovich.bankoperation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDto {

    @NotEmpty(message = "the login field must not be empty")
    @Size(min = 2, max = 100, message = "from 2 to 100 symbols")
    @Schema(description = "User login", example = "some login")
    private String login;

    @NotEmpty(message = "the password field must not be empty")
    @Size(min = 4, max = 100, message = "password must be between 4 and 100 characters")
    @Schema(description = "User password", example = "some password")
    private String password;
}
