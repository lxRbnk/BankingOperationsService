package org.rubnikovich.bankoperation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.dto.PhoneUpdateDto;
import org.rubnikovich.bankoperation.dto.UserPhoneNumberDto;
import org.rubnikovich.bankoperation.entity.User;
import org.rubnikovich.bankoperation.entity.UserPhoneNumber;
import org.rubnikovich.bankoperation.repository.PhoneRepository;
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
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public boolean phoneExists(String phone) {
        return phoneRepository.existsByPhone(phone);
    }

    public ResponseEntity<List<String>> getAllUserPhones(String token) {
        String login = jwtUtil.getLogin(token);
        Long userId = userService.getUserIdByLogin(login);
        List<UserPhoneNumber> phones = phoneRepository.findAllByUserId(userId);
        List<String> phoneStrings = phones.stream()
                .map(UserPhoneNumber::getPhone)
                .collect(Collectors.toList());
        log.info(PHONES + phones.size());
        return ResponseEntity.ok(phoneStrings);
    }

    public ResponseEntity<String> phoneAdd(String token, UserPhoneNumberDto phoneDto, BindingResult bindingResult) {
        ResponseEntity<String> errorResponse = handleBindingErrors(bindingResult, FAILED_ADD_PHONE);
        if (errorResponse != null) {
            return errorResponse;
        }
        String login = jwtUtil.getLogin(token);
        User user = userService.getByLogin(login);
        if (phoneRepository.existsByPhone(phoneDto.getPhone())) {
            log.warn(FAILED_ADD_PHONE + phoneDto.getPhone());
            return ResponseEntity.badRequest().body(FAILED_ADD_PHONE + ALREADY_EXISTS);
        }
        UserPhoneNumber phone = new UserPhoneNumber();
        phone.setUser(user);
        phone.setPhone(phoneDto.getPhone());
        phoneRepository.save(phone);
        log.info(PHONE_ADDED + phoneDto.getPhone());
        return ResponseEntity.ok().body(PHONE_ADDED);
    }

    public ResponseEntity<String> phoneUpdate(String token, PhoneUpdateDto updateDto,
                                              BindingResult bindingResult) {
        ResponseEntity<String> errorResponse = handleBindingErrors(bindingResult, FAILED_UPDATED_PHONE);
        if (errorResponse != null) {
            return errorResponse;
        }
        String login = jwtUtil.getLogin(token);
        Long userId = userService.getUserIdByLogin(login);
        updateDto.setUserId(userId);
        if (!phoneRepository.existsByPhone(updateDto.getCurrentPhone().getPhone()) ||
                phoneRepository.existsByPhone(updateDto.getNewPhone().getPhone())) {
            log.info(FAILED_UPDATED_PHONE);
            return ResponseEntity.badRequest().body(FAILED_UPDATED_PHONE);
        }
        UserPhoneNumber currentPhone = phoneRepository.findByPhone(updateDto.getCurrentPhone().getPhone());
        UserPhoneNumber phone = convertToUserPhone(updateDto);
        phone.setId(currentPhone.getId());
        phoneRepository.save(phone);
        log.info(PHONE_UPDATED + updateDto.getCurrentPhone().getPhone(),
                updateDto.getNewPhone().getPhone(), login);
        return ResponseEntity.ok().body(PHONE_UPDATED);
    }

    @Transactional
    public ResponseEntity<String> phoneDelete(String token, UserPhoneNumberDto phoneDto) {
        String login = jwtUtil.getLogin(token);
        Long userId = userService.getUserIdByLogin(login);
        String phoneStr = phoneDto.getPhone();
        List<UserPhoneNumber> phones = phoneRepository.findAllByUserId(userId);
        List<String> phoneStrings = phones.stream().map(UserPhoneNumber::getPhone).toList();
        if (phoneStrings.contains(phoneStr)) {
            phoneRepository.deleteByPhone(phoneStr);
            log.info(DELETED + phoneStr);
            return ResponseEntity.ok().body(DELETED + phoneStr);
        }
        log.warn(FAILED_DELETE_PHONE + phoneStr);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND + phoneStr);
    }

    private UserPhoneNumber convertToUserPhone(PhoneUpdateDto updateDto) {
        UserPhoneNumber phone = new UserPhoneNumber();
        User user = userRepository.findById(updateDto.getUserId()).orElseThrow();
        phone.setUser(user);
        phone.setPhone(updateDto.getNewPhone().getPhone());
        phone.setId(phone.getId());
        return phone;
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
