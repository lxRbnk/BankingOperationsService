package org.rubnikovich.bankoperation.service;

import lombok.RequiredArgsConstructor;
import org.rubnikovich.bankoperation.entity.User;
import org.rubnikovich.bankoperation.repository.UserRepository;
import org.rubnikovich.bankoperation.security.UsersDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.rubnikovich.bankoperation.config.ApiConstant.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = repository.findByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }
        return new UsersDetails(user.get());
    }
}

