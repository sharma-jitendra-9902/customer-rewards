package com.customer.loyalty.program.exception;


public class RewardCalculationException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public RewardCalculationException(String message) {
        super(message);
    }

    public RewardCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
