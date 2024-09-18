package org.rubnikovich.bankoperation.repository;

import org.rubnikovich.bankoperation.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    List<User> findAll();

    Page<User> findAllByBirthDateAfter(LocalDate birthDate, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.lastName LIKE %:lastName%")
    Page<User> findByLastNameLike(@Param("lastName") String lastName, Pageable pageable); //like

}
