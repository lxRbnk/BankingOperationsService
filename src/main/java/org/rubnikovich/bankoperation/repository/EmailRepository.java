package org.rubnikovich.bankoperation.repository;

import org.rubnikovich.bankoperation.entity.UserEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<UserEmail, Long> {

    boolean existsByEmail(String email);

    List<UserEmail> findAllByUserId(Long userId);

    void deleteByEmail(String email);

    UserEmail findByEmail(String email);

    Page<UserEmail> findByEmail(String email, Pageable pageable);

}
