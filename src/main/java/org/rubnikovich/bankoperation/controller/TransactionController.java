package org.rubnikovich.bankoperation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.dto.TransactionDto;
import org.rubnikovich.bankoperation.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Make a transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction made successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<String> makeTransaction(@RequestHeader("Authorization") String token,
                                          @RequestBody TransactionDto transactionDto) {
        return transactionService.makeTransaction(transactionDto, token);
    }

    @Operation(summary = "Get all transactions of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/user")
    public ResponseEntity<List<TransactionDto>> getAllUserTransactions(@RequestHeader("Authorization") String token) {
        return transactionService.getAllUserTransactions(token);
    }

    @Operation(summary = "Get all transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

}
