package com.customer.loyalty.program.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.customer.loyalty.program.dto.MonthlyReward;
import com.customer.loyalty.program.dto.RewardDetailsResponse;
import com.customer.loyalty.program.dto.RewardResponse;
import com.customer.loyalty.program.entity.Transaction;
import com.customer.loyalty.program.exception.RewardCalculationException;
import com.customer.loyalty.program.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private RewardService rewardService;
    

    @Test
    void testCalculateRewardsForCustomer_success() {
        String customerId = "C001";
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(3).withDayOfMonth(1);
        
        List<Transaction> mockTransactions = List.of(
            new Transaction(1L, customerId, 120.0, startDate.plusDays(5)), // earns 90
            new Transaction(2L, customerId, 70.0, startDate.plusDays(10))   // earns 20
        );

        when(repository.findByCustomerIdAndTransactionDateRange(eq(customerId), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(mockTransactions);

        RewardDetailsResponse response = rewardService.calculateRewardsForCustomer(customerId, null, null);

        assertEquals(customerId, response.getCustomerId());
        assertEquals(110, response.getTotalPoints());
        assertEquals(2, response.getTransactions().size());
        assertEquals(1, response.getMonthlyRewards().size());

        MonthlyReward monthlyReward = response.getMonthlyRewards().get(0);
        assertTrue(monthlyReward.getMonth().toUpperCase().contains(String.valueOf(startDate.getMonth())));
        assertEquals(110, monthlyReward.getPoints());
    }

    @Test
    void testCalculateRewardsForCustomer_noTransactions() {
        String customerId = "C999";

        when(repository.findByCustomerIdAndTransactionDateRange(eq(customerId), any(LocalDate.class),any(LocalDate.class)))
            .thenReturn(Collections.emptyList());

        RewardDetailsResponse response = rewardService.calculateRewardsForCustomer(customerId, null, null);

        assertEquals(customerId, response.getCustomerId());
        assertEquals(0, response.getTotalPoints());
        assertTrue(response.getTransactions().isEmpty());
        assertTrue(response.getMonthlyRewards().isEmpty());
    }

    @Test
    void testCalculateRewardsForCustomer_throwsCustomException() {
        String customerId = "C001";

        when(repository.findByCustomerIdAndTransactionDateRange(anyString(), any(), any()))
            .thenThrow(new RuntimeException("DB error"));

        RewardCalculationException ex = assertThrows(RewardCalculationException.class, () ->
            rewardService.calculateRewardsForCustomer(customerId, null, null));

        assertTrue(ex.getMessage().contains("Error calculating rewards for customer"));
    }

    @Test
    void testCalculateRewards_returnsSortedRewardsWithCorrectPoints() {
        
        List<Transaction> mockTransactions = List.of(
                new Transaction(1L, "C002", 200.0, LocalDate.of(2025, 2, 5)),
                new Transaction(2L, "C001", 120.0, LocalDate.of(2025, 1, 10)),
                new Transaction(3L, "C001", 80.0, LocalDate.of(2025, 2, 10))
        );

        when(repository.findAllFromLastThreeMonths())
                .thenReturn(mockTransactions);

        List<RewardResponse> result = rewardService.calculateRewards();

        assertEquals(2, result.size());

        assertEquals("C001", result.get(0).getCustomerId());
        assertEquals("C002", result.get(1).getCustomerId());

        assertEquals(120, result.get(0).getTotalPoints());
        assertEquals(250, result.get(1).getTotalPoints());

        assertEquals(2, result.get(0).getMonthlyRewards().size()); // C001 has 2 months
        assertEquals("JANUARY 2025", result.get(0).getMonthlyRewards().get(0).getMonth());
        assertEquals("FEBRUARY 2025", result.get(0).getMonthlyRewards().get(1).getMonth());
    }
    
    @Test
    void testCalculatePoints_LessThan50() {
        assertEquals(0, rewardService.calculatePoints(49.99));
    }

    @Test
    void testCalculatePoints_Exactly50() {
        assertEquals(0, rewardService.calculatePoints(50.0));
    }

    @Test
    void testCalculatePoints_Between50And100() {
        assertEquals(30, rewardService.calculatePoints(80.0)); // 80 - 50 = 30
    }

    @Test
    void testCalculatePoints_Exactly100() {
        assertEquals(50, rewardService.calculatePoints(100.0)); // Flat 50
    }


}