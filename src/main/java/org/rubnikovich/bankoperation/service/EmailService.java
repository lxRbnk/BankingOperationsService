package org.rubnikovich.bankoperation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.dto.EmailUpdateDto;
import org.rubnikovich.bankoperation.dto.UserEmailDto;
import org.rubnikovich.bankoperation.entity.User;
import org.rubnikovich.bankoperation.entity.UserEmail;
import org.rubnikovich.bankoperation.repository.EmailRepository;
import org.rubnikovich.bankoperation.repository.UserRepository;
import org.rubnikovich.bankoperation.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

import static org.rubnikovich.bankoperation.config.ApiConstant.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public boolean emailExists(String email) {
        return emailRepository.existsByEmail(email);
    }

    public ResponseEntity<List<String>> getAllUserEmails(String token) {
        String login = jwtUtil.getLogin(token);
        Long userId = userService.getUserIdByLogin(login);
        List<UserEmail> emails = emailRepository.findAllByUserId(userId);
        List<String> emailStrings = emails.stream()
                .map(UserEmail::getEmail)
                .collect(Collectors.toList());
        log.info(EMAILS + emails.size());
        return ResponseEntity.ok(emailStrings);
    }

    public ResponseEntity<String> emailAdd(String token, UserEmailDto emailDto, BindingResult bindingResult) {
        ResponseEntity<String> errorResponse = handleBindingErrors(bindingResult, FAILED_ADD_EMAIL);
        if (errorResponse != null) {
            return errorResponse;
        }
        String login = jwtUtil.getLogin(token);
        User user = userService.getByLogin(login);
        if (emailRepository.existsByEmail(emailDto.getEmail())) {
            log.warn(FAILED_ADD_EMAIL + emailDto.getEmail());
            return ResponseEntity.badRequest().body(FAILED_ADD_EMAIL + ALREADY_EXISTS);
        }
        UserEmail email = new UserEmail();
        email.setUser(user);
        email.setEmail(emailDto.getEmail());
        emailRepository.save(email);
        log.info(EMAIL_ADDED + emailDto.getEmail());
        return ResponseEntity.ok().body(EMAIL_ADDED);
    }

    public ResponseEntity<String> emailUpdate(String token, EmailUpdateDto updateDto,
                                              BindingResult bindingResult) {
        ResponseEntity<String> errorResponse = handleBindingErrors(bindingResult, FAILED_UPDATED_EMAIL);
        if (errorResponse != null) {
            return errorResponse;
        }
        String login = jwtUtil.getLogin(token);
        Long userId = userService.getUserIdByLogin(login);
        updateDto.setUserId(userId);
        if (!emailRepository.existsByEmail(updateDto.getCurrentEmail().getEmail()) ||
                emailRepository.existsByEmail(updateDto.getNewEmail().getEmail())) {
            log.info(FAILED_UPDATED_EMAIL);
            return ResponseEntity.badRequest().body(FAILED_UPDATED_EMAIL);
        }
        UserEmail currentEmail = emailRepository.findByEmail(updateDto.getCurrentEmail().getEmail());
        UserEmail email = convertToUserEmail(updateDto);
        email.setId(currentEmail.getId());
        emailRepository.save(email);
        log.info(EMAIL_UPDATED + updateDto.getCurrentEmail().getEmail(),
                updateDto.getNewEmail().getEmail(), login);
        return ResponseEntity.ok().body(EMAIL_UPDATED);
    }

    @Transactional
    public ResponseEntity<String> emailDelete(String token, UserEmailDto emailDto) {
        String login = jwtUtil.getLogin(token);
        Long userId = userService.getUserIdByLogin(login);
        String emailStr = emailDto.getEmail();
        List<UserEmail> emails = emailRepository.findAllByUserId(userId);
        List<String> emailStrings = emails.stream().map(UserEmail::getEmail).toList();
        if (emailStrings.contains(emailStr)) {
            emailRepository.deleteByEmail(emailStr);
            log.info(DELETED + emailStr);
            return ResponseEntity.ok().body(DELETED + emailStr);
        }
        log.warn(FAILED_DELETE_EMAIL + emailStr, login);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND + emailStr);
    }

    private UserEmail convertToUserEmail(EmailUpdateDto updateDto) {
        UserEmail email = new UserEmail();
        User user = userRepository.findById(updateDto.getUserId()).orElseThrow();
        email.setUser(user);
        email.setEmail(updateDto.getNewEmail().getEmail());
        email.setId(email.getId());
        return email;
    }

    private ResponseEntity<String> handleBindingErrors(BindingResult bindingResult, String message) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            log.warn(message + errors);
            return ResponseEntity.badRequest().body(message + errors);
        }
        return null;
    }
}
