package org.rubnikovich.bankoperation.repository;

import org.rubnikovich.bankoperation.entity.UserPhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneRepository extends JpaRepository<UserPhoneNumber, Long> {

    boolean existsByPhone(String phoneNumber);

    List<UserPhoneNumber> findAllByUserId(Long userId);

    void deleteByPhone(String phone);

    UserPhoneNumber findByPhone(String phone);
}
