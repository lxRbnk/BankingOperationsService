package org.rubnikovich.bankoperation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "must not be empty")
    @Size(min = 2, max = 100, message = "should be between 2 and 100 characters")
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_day", nullable = false)
    private LocalDate birthDate;

    @Column(name = "balance")
    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    private BigDecimal balance;

    @DecimalMin(value = "0.0", message = "initial Deposit > 0")
    @Column(name = "initialDeposit", nullable = false)
    private BigDecimal initialDeposit;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private List<Transaction> sentTransactions;

    @JsonIgnore
    @OneToMany(mappedBy = "recipient")
    private List<Transaction> receivedTransactions;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEmail> emails;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPhoneNumber> phones;
}

