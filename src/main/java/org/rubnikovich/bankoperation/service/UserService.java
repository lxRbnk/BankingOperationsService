package org.rubnikovich.bankoperation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.dto.UserDto;
import org.rubnikovich.bankoperation.entity.User;
import org.rubnikovich.bankoperation.entity.UserEmail;
import org.rubnikovich.bankoperation.entity.UserPhoneNumber;
import org.rubnikovich.bankoperation.repository.EmailRepository;
import org.rubnikovich.bankoperation.repository.PhoneRepository;
import org.rubnikovich.bankoperation.repository.UserRepository;
import org.rubnikovich.bankoperation.security.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.rubnikovich.bankoperation.config.ApiConstant.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final JwtUtil jwtUtil;

    public User getByLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    public ResponseEntity<List<UserDto>> getAll() {
        List<User> users = repository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(toUserDto(user));
        }
        log.info(FETCHING_ALL_USERS);
        return ResponseEntity.ok().body(usersDto);
    }

    @Transactional
    public void create(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        List<UserPhoneNumber> phoneNumbers = user.getPhones();
        List<UserEmail> userEmails = user.getEmails();
        for (UserPhoneNumber phone : phoneNumbers) {
            phone.setUser(user);
        }
        for (UserEmail email : userEmails) {
            email.setUser(user);
        }
        repository.save(user);
        phoneRepository.saveAll(phoneNumbers);
        emailRepository.saveAll(userEmails);
    }

    public ResponseEntity<String> delete(String token) {
        User user;
        try {
            String login = jwtUtil.getLogin(token);
            user = repository.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        } catch (UsernameNotFoundException e) {
            log.warn(USER_DELETION_FAILED);
            return ResponseEntity.badRequest().body(USER_DELETION_FAILED);
        }
        repository.deleteById(user.getId());
        log.info(DELETED + user.getLogin());
        return ResponseEntity.ok().body(DELETED + user.getLogin());
    }

    public Long getUserIdByLogin(String login) {
        return repository.findByLogin(login)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    public ResponseEntity<Page<UserDto>> getAllByBirthDateAfter(LocalDate birthDate, Pageable pageable) {
        Page<User> usersPage = repository.findAllByBirthDateAfter(birthDate, pageable);
        return ResponseEntity.ok().body(usersPage.map(this::toUserDto));
    }

    public ResponseEntity<Page<UserDto>> getAllUsers(LocalDate birthDate, String phone, String lastName, String email, Pageable pageable) {
        Page<User> usersPage;
        if (email != null) {
            Page<UserEmail> emailPage = emailRepository.findByEmail(email, pageable);
            List<User> users = emailPage.stream().map(UserEmail::getUser).collect(Collectors.toList());
            usersPage = new PageImpl<>(users, pageable, emailPage.getTotalElements());
        } else if (lastName != null) {
            usersPage = repository.findByLastNameLike(lastName, pageable);
        } else {
            usersPage = repository.findAll(pageable);
        }
        return ResponseEntity.ok().body(usersPage.map(this::toUserDto));
    }

    private UserDto toUserDto(User user) {
        return UserDto.builder()
                .login(user.getLogin())
                .balance(user.getBalance())
                .initialDeposit(user.getInitialDeposit())
                .birthDate(user.getBirthDate())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
// http://localhost:8080/users/date?birthDate=1990-01-01
// http://localhost:8080/users/find?email=mail@mail.com&page=0&size=10&sort=email,asc
// http://localhost:8080/users/find?lastName=bob&page=0&size=10&sort=lastName,asc
