package com.customer.loyalty.program.utils;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.customer.loyalty.program.entity.Transaction;
import com.customer.loyalty.program.exception.RewardCalculationException;

@Component
public class TransactionValidator {
    public void validate(Transaction txn) {
        if (txn.getAmount() == null) {
            throw new RewardCalculationException("Transaction amount cannot be null.");
        }
        if (txn.getTransactionDate() != null && txn.getTransactionDate().isAfter(LocalDate.now())) {
            throw new RewardCalculationException("Transaction date cannot be in the future.");
        }
    }
}
