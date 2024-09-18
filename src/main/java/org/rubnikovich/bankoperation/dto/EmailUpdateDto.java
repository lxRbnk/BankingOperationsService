package org.rubnikovich.bankoperation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailUpdateDto {

    @Valid
    private UserEmailDto currentEmail;

    @Valid
    private UserEmailDto newEmail;

    @Schema(hidden = true)
    private Long id;

    @Schema(hidden = true)
    private Long userId;
}
