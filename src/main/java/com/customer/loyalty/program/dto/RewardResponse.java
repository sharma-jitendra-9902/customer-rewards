package com.customer.loyalty.program.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RewardResponse implements Serializable{
  
	
	private static final long serialVersionUID = 1L;
	
	private String customerId;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;
    
}
