package com.customer.loyalty.program.repository;

import com.customer.loyalty.program.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	@Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :startDate")
    List<Transaction> findAllFromLastThreeMonths(@Param("startDate") LocalDate startDate);

	@Query("SELECT t FROM Transaction t WHERE t.customerId = :customerId AND t.transactionDate >= :startDate")
	List<Transaction> findByCustomerIdAndTransactionDateAfter(
	        @Param("customerId") String customerId,
	        @Param("startDate") LocalDate startDate);
}