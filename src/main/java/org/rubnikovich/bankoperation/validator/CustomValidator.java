package org.rubnikovich.bankoperation.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.entity.User;
import org.rubnikovich.bankoperation.entity.UserEmail;
import org.rubnikovich.bankoperation.entity.UserPhoneNumber;
import org.rubnikovich.bankoperation.service.DetailsService;
import org.rubnikovich.bankoperation.service.EmailService;
import org.rubnikovich.bankoperation.service.PhoneService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomValidator implements Validator {
    private final DetailsService detailsService;
    private final PhoneService phoneService;
    private final EmailService emailService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            detailsService.loadUserByUsername(user.getLogin());
            errors.rejectValue("login", "", "Login already exists");
            log.warn("Login already exists: {}", user.getLogin());
        } catch (UsernameNotFoundException ignored) {
        }
        if (user.getEmails() != null && !user.getEmails().isEmpty()) {
            for (UserEmail userEmail : user.getEmails()) {
                if (emailService.emailExists(userEmail.getEmail())) {
                    errors.rejectValue("emails", "", "Email already exists");
                    log.warn("Email already exists: {}", userEmail.getEmail());
                    break;
                }
            }
        }
        if (user.getPhones() != null && !user.getPhones().isEmpty()) {
            for (UserPhoneNumber userPhone : user.getPhones()) {
                if (phoneService.phoneExists(userPhone.getPhone())) {
                    errors.rejectValue("phones", "", "Phone number already exists");
                    log.warn("Phone number already exists: {}", userPhone.getPhone());
                    break;
                }
            }
        }
    }

}
