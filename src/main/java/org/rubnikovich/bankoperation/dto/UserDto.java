package org.rubnikovich.bankoperation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.rubnikovich.bankoperation.entity.UserEmail;
import org.rubnikovich.bankoperation.entity.UserPhoneNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class UserDto {

    @NotEmpty(message = "Login cannot be empty")
    @Size(min = 2, max = 100, message = "Login must be between 2 and 100 characters")
    @Schema(description = "User login", example = "some login")
    private String login;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
    @Schema(description = "User password", example = "some password")
    private String password;

    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Schema(description = "User first name", example = "some first name")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Schema(description = "User last name", example = "some last name")
    private String lastName;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    @Schema(description = "User birth date", example = "1990-10-19")
    private LocalDate birthDate;

    @NotEmpty(message = "Emails cannot be empty")
    @Schema(description = "User email addresses", example = "[{\"email\": \"someMail@mail.com\"}, {\"email\": \"anotherMail@mail.com\"}]")
    private List<@NotNull(message = "Email cannot be null") UserEmail> emails;

    @NotEmpty(message = "Phones cannot be empty")
    @Schema(description = "User phone numbers", example = "[{\"phone\": \"+123456789\"}, {\"phone\": \"+987654321\"}]")
    private List<@NotNull(message = "Phone number cannot be null") UserPhoneNumber> phones;

    @Schema(hidden = true)
    private BigDecimal balance;

    @NotNull(message = "Initial deposit cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Initial deposit must be greater than zero")
    @Schema(description = "Initial deposit amount", example = "100")
    private BigDecimal initialDeposit;
}


