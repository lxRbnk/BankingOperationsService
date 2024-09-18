package org.rubnikovich.bankoperation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {

    @Schema(hidden = true)
    private long id;

    @Schema(hidden = true)
    private long sender;

    @NotNull(message = "Recipient ID cannot be null")
    @Schema(description = "recipient id", example = "1")
    private long recipientId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Schema(description = "transfer amount", example = "30")
    private BigDecimal amount;

    @Schema(hidden = true)
    private LocalDateTime date;
}
