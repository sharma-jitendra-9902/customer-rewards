package com.customer.loyalty.program.service;

import org.springframework.stereotype.Service;

import com.customer.loyalty.program.entity.Transaction;
import com.customer.loyalty.program.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction insertTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
