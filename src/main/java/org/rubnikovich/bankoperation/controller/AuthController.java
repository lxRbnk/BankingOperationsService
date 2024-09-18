package org.rubnikovich.bankoperation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.rubnikovich.bankoperation.dto.AuthenticationDto;
import org.rubnikovich.bankoperation.dto.UserDto;
import org.rubnikovich.bankoperation.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid UserDto userDto,
                                                                  BindingResult bindingResult) {
        return authService.registrationUser(userDto, bindingResult);
    }

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody @Valid AuthenticationDto authenticationDto,
                                            BindingResult bindingResult) {
        return authService.loginUser(authenticationDto, bindingResult);
    }
}


