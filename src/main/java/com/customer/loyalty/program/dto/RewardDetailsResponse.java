package com.customer.loyalty.program.dto;

import java.time.LocalDate;
import java.util.List;

import com.customer.loyalty.program.entity.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RewardDetailsResponse {

	private String customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;
    private List<Transaction> transactions; 
    
}
