package org.rubnikovich.bankoperation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.dto.TransactionDto;
import org.rubnikovich.bankoperation.entity.Transaction;
import org.rubnikovich.bankoperation.entity.User;
import org.rubnikovich.bankoperation.repository.TransactionRepository;
import org.rubnikovich.bankoperation.repository.UserRepository;
import org.rubnikovich.bankoperation.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.rubnikovich.bankoperation.config.ApiConstant.*;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDto> transactionsDto = transactions.stream()
                .map(this::toTransactionDto)
                .collect(Collectors.toList());
        return transactions.isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok().body(transactionsDto);
    }

    public ResponseEntity<List<TransactionDto>> getAllUserTransactions(String token) {
        String login = jwtUtil.getLogin(token);
        User user = userRepository.findByLogin(login).orElseThrow();
        Long id = user.getId();
        List<Transaction> transactions = transactionRepository.getAllByRecipientIdOrSenderId(id, id);
        List<TransactionDto> transactionsDto = transactions.stream()
                .map(this::toTransactionDto)
                .collect(Collectors.toList());
        return transactions.isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok().body(transactionsDto);
    }

    public ResponseEntity<String> makeTransaction(TransactionDto transactionDto, String token) {
        String login = jwtUtil.getLogin(token);
        User sender;
        User recipient;
        try {
            sender = userRepository.findByLogin(login)
                    .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND));
            transactionDto.setSender(sender.getId());
            recipient = userRepository.findById(transactionDto.getRecipientId())
                    .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND));
        } catch (NoSuchElementException e) {
            log.warn(USER_NOT_FOUND);
            return ResponseEntity.badRequest()
                    .body(TRANSACTION_FAILED + USER_NOT_FOUND);
        }
        BigDecimal senderMoney = sender.getBalance();
        BigDecimal recipientMoney = recipient.getBalance();
        BigDecimal amount = transactionDto.getAmount();
        BigDecimal result = senderMoney.subtract(amount);
        if (result.compareTo(BigDecimal.ZERO) < 0 ||
                amount.compareTo(BigDecimal.ZERO) < 0) {
            log.warn(TRANSACTION_FAILED_BALANCE);
            return ResponseEntity.badRequest()
                    .body(TRANSACTION_FAILED_BALANCE);
        }
        sender.setBalance(senderMoney.subtract(amount));
        recipient.setBalance(recipientMoney.add(amount));
        Transaction transaction = toTransactions(transactionDto);
        transactionRepository.save(transaction);
        userRepository.save(recipient);
        userRepository.save(sender);
        log.info(TRANSACTION_SUCCESSFULLY);
        return ResponseEntity.ok().body(TRANSACTION_SUCCESSFULLY);
    }

    private TransactionDto toTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setDate(transaction.getDate());
        transactionDto.setId(transaction.getId());
        transactionDto.setSender(transaction.getSender().getId());
        transactionDto.setRecipientId(transaction.getRecipient().getId());
        return transactionDto;
    }

    private Transaction toTransactions(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(transactionDto.getDate());
        transaction.setSender(userRepository.findById(transactionDto.getSender()).orElseThrow());
        transaction.setRecipient(userRepository.findById(transactionDto.getRecipientId()).orElseThrow());
        return transaction;
    }
}
