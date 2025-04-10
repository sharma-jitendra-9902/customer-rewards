package com.customer.loyalty.program.service;


import com.customer.loyalty.program.dto.MonthlyReward;
import com.customer.loyalty.program.dto.RewardDetailsResponse;
import com.customer.loyalty.program.dto.RewardResponse;
import com.customer.loyalty.program.entity.Transaction;
import com.customer.loyalty.program.exception.RewardCalculationException;
import com.customer.loyalty.program.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class RewardService {

	private final TransactionRepository repository;

	public RewardService(TransactionRepository repository) {
		this.repository = repository;
	}

	/**
	 * Calculates reward points for all customers based on their transaction history
	 * within the last 3 months.
	 * 
	 * Step: 1. Filters transactions to include only those occurring in the last 3 months, starting from the first day of the 3rd month ago up to the current date. 
	 * Step: 2. Groups the filtered transactions by customer ID. 
	 * Step: 3. For each customer: 
	 * - Calculates reward points per transaction. 
	 * - Aggregates points per month using an TreeMap for Month. 
	 * - Calculates total reward points. 
	 * Step: 4. Creates a list of RewardResponse objects, each containing: 
	 * - Customer ID 
	 * - Monthly reward breakdown 
	 * - Total reward points
	 *
	 * @return List of RewardResponse objects representing reward points per
	 *         customer.
	 */
	public List<RewardResponse> calculateRewards() {

	    LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3).withDayOfMonth(1);
	    List<Transaction> recentTransactions = repository.findAllFromLastThreeMonths(threeMonthsAgo);

	    Map<String, List<Transaction>> groupedByCustomer = groupTransactionsByCustomer(recentTransactions);

	    return groupedByCustomer.entrySet().stream()
	            .sorted(Map.Entry.comparingByKey())
	            .map(this::buildRewardResponse)
	            .toList();
	}

	private Map<String, List<Transaction>> groupTransactionsByCustomer(List<Transaction> transactions) {
		return transactions.stream().collect(Collectors.groupingBy(Transaction::getCustomerId));
	}

	private RewardResponse buildRewardResponse(Map.Entry<String, List<Transaction>> entry) {
		String customerId = entry.getKey();
		List<Transaction> customerTxns = entry.getValue();

		Map<YearMonth, Integer> monthlyPoints = new TreeMap<>();
		int totalPoints = 0;

		for (Transaction txn : customerTxns) {
			int points = calculatePoints(txn.getAmount());
			YearMonth yearMonth = YearMonth.from(txn.getTransactionDate());
			monthlyPoints.merge(yearMonth, points, Integer::sum);
			totalPoints += points;
		}

		List<MonthlyReward> monthlyRewards = buildMonthlyRewards(monthlyPoints);

		return new RewardResponse(customerId, monthlyRewards, totalPoints);
	}

	/**
	 * Calculates reward points based on the transaction amount using the following
	 * rules: 
	 * - No points for amounts $50 or less. 
	 * - 1 point for every dollar spent over $50 up to $100. 
	 * - 2 points for every dollar spent over $100.
	 *
	 * Examples: 
	 * - $120 → (20 * 2) + 50 = 90 points 
	 * - $80 → (80 - 50) = 30 points
	 * - $45 → 0 points
	 *
	 * @param amount The transaction amount
	 * @return The calculated reward points
	 */
	public int calculatePoints(Double amount) { // round of need to be done 130.4 = 130 
		int points = 0;
		if (amount > 100) {
			points += (int) ((amount - 100) * 2);
			points += 50; // For the $50 between 50 and 100
		} else if (amount > 50) {
			points += (int) (amount - 50);
		}
		return points;
	}

	/**
	 * Calculates reward details for a specific customer within a given date range.
	 * 
	 * Step: 1. If startDate or endDate is not provided, defaults to the last 3 months. 
	 * Step: 2. Filters transactions to include only those within the specified date range.
	 * Step: 3. Calculates reward points for each transaction based on the amount: 
	 * - 2 points per dollar over $100 
	 * - 1 point per dollar over $50 (up to $100) 
	 * Step: 4. Groups points by month and calculates the total. 6. Builds a response
	 * including: 
	 * - Customer ID 
	 * - Start and end date 
	 * - Monthly reward breakdown ("MONTH YYYY" format) 
	 * - Total reward points 
	 * - List of filtered transactions
	 *
	 * @param customerId ID of the customer
	 * @param startDate  Start of the reward calculation period (nullable)
	 * @param endDate    End of the reward calculation period (nullable)
	 * @return RewardDetailsResponse with full reward summary
	 */
	public RewardDetailsResponse calculateRewardsForCustomer(String customerId, LocalDate startDate,
			LocalDate endDate) {
		try {
			Optional<List<Transaction>> customerTransactions = getTransactionsForCustomer(customerId);

			LocalDateRange dateRange = resolveDateRange(startDate, endDate);

			List<Transaction> filteredTransactions = filterTransactionsByDate(
					customerTransactions.orElse(Collections.emptyList()), dateRange);

			Map<YearMonth, Integer> monthlyPoints = calculateMonthlyPoints(filteredTransactions);

			List<MonthlyReward> monthlyRewards = buildMonthlyRewards(monthlyPoints);

			int totalPoints = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();

			return new RewardDetailsResponse(customerId, dateRange.start(), dateRange.end(), monthlyRewards,
					totalPoints, filteredTransactions);
		} catch (Exception e) {
			throw new RewardCalculationException("Error calculating rewards for customer: " + customerId, e);
		}
	}


	private Optional<List<Transaction>> getTransactionsForCustomer(String customerId) {
	    LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3).withDayOfMonth(1);
	    List<Transaction> transactions = repository.findByCustomerIdAndTransactionDateAfter(customerId, threeMonthsAgo);
	    return Optional.ofNullable(transactions.isEmpty() ? null : transactions);
	}

	private record LocalDateRange(LocalDate start, LocalDate end) {
	}

	private LocalDateRange resolveDateRange(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			endDate = LocalDate.now();
			startDate = endDate.minusMonths(3).withDayOfMonth(1);
		}
		return new LocalDateRange(startDate, endDate);
	}

	private List<Transaction> filterTransactionsByDate(List<Transaction> transactions, LocalDateRange range) {
		return transactions.stream().filter(txn -> !txn.getTransactionDate().isBefore(range.start())
				&& !txn.getTransactionDate().isAfter(range.end())).toList();
	}

	private Map<YearMonth, Integer> calculateMonthlyPoints(List<Transaction> transactions) {
		Map<YearMonth, Integer> monthlyPoints = new TreeMap<>();
		for (Transaction txn : transactions) {
			int points = calculatePoints(txn.getAmount());
			YearMonth yearMonth = YearMonth.from(txn.getTransactionDate());
			monthlyPoints.merge(yearMonth, points, Integer::sum);
		}
		return monthlyPoints;
	}

	private List<MonthlyReward> buildMonthlyRewards(Map<YearMonth, Integer> monthlyPoints) {
		return monthlyPoints.entrySet().stream()
				.map(entry -> new MonthlyReward(entry.getKey().getMonth().name() + " " + entry.getKey().getYear(),
						entry.getValue()))
				.toList();
	}

}