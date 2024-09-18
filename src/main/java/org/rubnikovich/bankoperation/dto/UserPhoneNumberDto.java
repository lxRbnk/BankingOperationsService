package org.rubnikovich.bankoperation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPhoneNumberDto {

    @Schema(hidden = true)
    private Long id;

    @Schema(description = "phone", example = "+123456789")
    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    private String phone;

    @Schema(hidden = true)
    private Long userId;
}