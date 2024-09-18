package org.rubnikovich.bankoperation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.dto.UserDto;
import org.rubnikovich.bankoperation.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/show")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return userService.getAll();
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token) {
        return userService.delete(token);
    }

    @Operation(summary = "Get users by birth date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/date")
    public ResponseEntity<Page<UserDto>> getUsersByBirthDate(
            @RequestParam("birthDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
            Pageable pageable) {
        return userService.getAllByBirthDateAfter(birthDate, pageable);
    }

    @Operation(summary = "Find users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Ban request")
    })
    @GetMapping("/find")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            Pageable pageable) {
        return userService.getAllUsers(birthDate, phone, lastName, email, pageable);
    }
}
