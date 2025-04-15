package com.customer.loyalty.program.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.customer.loyalty.program.entity.Transaction;
import com.customer.loyalty.program.exception.InvalidTransactionException;
import com.customer.loyalty.program.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public Transaction insertTransaction(Transaction transaction) {

		// Validation: amount must not be null
		if (transaction.getAmount() == null) {
			log.error("Transaction amount is null");
			throw new InvalidTransactionException("Amount must not be null.");
		}

		// Validation: transaction date must not be in the future
		if (transaction.getTransactionDate() != null &&
		    transaction.getTransactionDate().isAfter(LocalDate.now())) {
			log.error("Transaction date is in the future: {}", transaction.getTransactionDate());
			throw new InvalidTransactionException("Transaction date cannot be in the future.");
		}

		log.info("Saving transaction: {}", transaction);
		return transactionRepository.save(transaction);
	}
}
