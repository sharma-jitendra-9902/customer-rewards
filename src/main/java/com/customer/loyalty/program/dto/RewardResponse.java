package com.customer.loyalty.program.dto;

import java.io.Serializable;
import java.util.List;

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
public class RewardResponse implements Serializable{
  
	
	private static final long serialVersionUID = 1L;
	
	private String customerId;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;
    
}
