package org.rubnikovich.bankoperation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.dto.PhoneUpdateDto;
import org.rubnikovich.bankoperation.dto.UserPhoneNumberDto;
import org.rubnikovich.bankoperation.service.PhoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone")
@RequiredArgsConstructor
@Slf4j
public class PhoneController {
    private final PhoneService phoneService;

    @Operation(summary = "Get all phones for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phones retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping
    public ResponseEntity<List<String>> getAllUserPhones(@RequestHeader("Authorization")
                                                         String token) {
        return phoneService.getAllUserPhones(token);
    }

    @Operation(summary = "Add an phone for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<String> addPhone(@RequestHeader("Authorization") String token,
                                           @RequestBody @Valid UserPhoneNumberDto phoneDto,
                                           BindingResult bindingResult) {
        return phoneService.phoneAdd(token, phoneDto, bindingResult);
    }

    @Operation(summary = "Update an phone for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping()
    public ResponseEntity<String> update(@RequestHeader("Authorization") String token,
                                         @RequestBody @Valid PhoneUpdateDto updateDto,
                                         BindingResult bindingResult) {
        return phoneService.phoneUpdate(token, updateDto, bindingResult);
    }

    @Operation(summary = "Delete an phone for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token,
                                         @RequestBody UserPhoneNumberDto phoneDto) {
        return phoneService.phoneDelete(token, phoneDto);
    }
}
