package com.customer.loyalty.program.repository;

import com.customer.loyalty.program.entity.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAll();
}