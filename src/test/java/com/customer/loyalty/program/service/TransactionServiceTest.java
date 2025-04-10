package com.customer.loyalty.program.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.customer.loyalty.program.entity.Transaction;
import com.customer.loyalty.program.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	void testInsertTransaction_success() {
		Transaction inputTransaction = new Transaction(1L, "C001", 120.0, LocalDate.of(2025, 4, 1));
		Transaction savedTransaction = new Transaction(1L, "C001", 120.0, LocalDate.of(2025, 4, 1));

		when(transactionRepository.save(inputTransaction)).thenReturn(savedTransaction);

		Transaction result = transactionService.insertTransaction(inputTransaction);

		assertNotNull(result);
		assertEquals("C001", result.getCustomerId());
		assertEquals(120.0, result.getAmount());
		assertEquals(LocalDate.of(2025, 4, 1), result.getTransactionDate());

		verify(transactionRepository, times(1)).save(inputTransaction);
	}
}
