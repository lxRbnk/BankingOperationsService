package org.rubnikovich.bankoperation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rubnikovich.bankoperation.entity.User;
import org.rubnikovich.bankoperation.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BalanceUpdater {

    private final UserRepository userRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void increaseBalance() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            BigDecimal currentBalance = user.getBalance();
            BigDecimal initialDeposit = user.getInitialDeposit();
            BigDecimal increaseAmount = initialDeposit.multiply(BigDecimal.valueOf(0.05));
            BigDecimal maxBalance = initialDeposit.multiply(BigDecimal.valueOf(2.07));
            BigDecimal newBalance = currentBalance.add(increaseAmount);
            if (newBalance.compareTo(maxBalance) > 0) {
                newBalance = maxBalance;
            }
            user.setBalance(newBalance.setScale(2, RoundingMode.HALF_UP));
            userRepository.save(user);
        }
    }
}