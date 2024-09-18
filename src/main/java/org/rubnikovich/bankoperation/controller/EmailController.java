package org.rubnikovich.bankoperation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.dto.EmailUpdateDto;
import org.rubnikovich.bankoperation.dto.UserEmailDto;
import org.rubnikovich.bankoperation.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService emailService;

    @Operation(summary = "Get all emails for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emails retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping
    public ResponseEntity<List<String>> getAllUserEmails(@RequestHeader("Authorization")
                                                         String token) {
        return emailService.getAllUserEmails(token);
    }

    @Operation(summary = "Add an email for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<String> addEmail(@RequestHeader("Authorization") String token,
                                           @RequestBody @Valid UserEmailDto emailDto,
                                           BindingResult bindingResult) {
        return emailService.emailAdd(token, emailDto, bindingResult);
    }

    @Operation(summary = "Update an email for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping()
    public ResponseEntity<String> update(@RequestHeader("Authorization") String token,
                                         @RequestBody @Valid EmailUpdateDto updateDto,
                                         BindingResult bindingResult) {
        return emailService.emailUpdate(token, updateDto, bindingResult);
    }

    @Operation(summary = "Delete an email for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token,
                                         @RequestBody UserEmailDto emailDto) {
        return emailService.emailDelete(token, emailDto);
    }
}
