package org.rubnikovich.bankoperation.repository;

import org.rubnikovich.bankoperation.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> getAllByRecipientIdOrSenderId(Long recipientId, Long senderId);
}
