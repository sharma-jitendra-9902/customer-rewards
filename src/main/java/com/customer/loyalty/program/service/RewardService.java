package com.customer.loyalty.program.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.customer.loyalty.program.dto.MonthlyReward;
import com.customer.loyalty.program.dto.RewardDetailsResponse;
import com.customer.loyalty.program.dto.RewardResponse;
import com.customer.loyalty.program.dto.TransactionDto;
import com.customer.loyalty.program.entity.Transaction;
import com.customer.loyalty.program.exception.InvalidDateRangeException;
import com.customer.loyalty.program.exception.InvalidTransactionException;
import com.customer.loyalty.program.exception.RewardCalculationException;
import com.customer.loyalty.program.repository.TransactionRepository;
import com.customer.loyalty.program.utils.ClassUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RewardService {

	private final TransactionRepository repository;

	public RewardService(TransactionRepository repository) {
		this.repository = repository;
	}

	/**
	 * Calculates reward points for all customers based on their transaction history
	 * within the last 3 months.
	 * 
	 * Step: 1. Filters transactions to include only those occurring in the last 3
	 * months, starting from the first day of the 3rd month ago up to the current
	 * date. Step: 2. Groups the filtered transactions by customer ID. Step: 3. For
	 * each customer: - Calculates reward points per transaction. - Aggregates
	 * points per month using an TreeMap for Month. - Calculates total reward
	 * points. Step: 4. Creates a list of RewardResponse objects, each containing: -
	 * Customer ID - Monthly reward breakdown - Total reward points
	 *
	 * @return List of RewardResponse objects representing reward points per
	 *         customer.
	 */
	public List<RewardResponse> calculateRewards() {
		List<Transaction> recentTransactions = repository.findAllFromLastThreeMonths();

		Map<String, List<Transaction>> groupedByCustomer = groupTransactionsByCustomer(recentTransactions);

		return groupedByCustomer.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(this::buildRewardResponse)
				.toList();
	}

	private Map<String, List<Transaction>> groupTransactionsByCustomer(List<Transaction> transactions) {

		return transactions.stream().collect(Collectors.groupingBy(Transaction::getCustomerId));
	}

	private RewardResponse buildRewardResponse(Map.Entry<String, List<Transaction>> entry) {

	    // Extract customerId (the key of the map entry)
	    String customerId = entry.getKey();
	    
	    // Get the list of transactions for the customer (the value of the map entry)
	    List<Transaction> customerTxns = entry.getValue();

	    // Create a map to store monthly points (key: YearMonth, value: total points for that month)
	    Map<YearMonth, Integer> monthlyPoints = new TreeMap<>();
	    
	    // Initialize the totalPoints counter
	    int totalPoints = 0;

	    // Loop through each transaction to calculate points
	    for (Transaction txn : customerTxns) {
	        
	        // Validate the transaction (this could check for things like validity of the amount)
	        validateTransaction(txn);
	        
	        // Calculate points for the transaction based on the amount (this can be a simple formula or logic)
	        int points = calculatePoints(txn.getAmount());
	        
	        // Get the YearMonth (year and month) from the transaction date
	        YearMonth yearMonth = YearMonth.from(txn.getTransactionDate());
	        
	        // Add the calculated points to the monthly points map (merging if the yearMonth already exists)
	        monthlyPoints.merge(yearMonth, points, Integer::sum);
	        
	        // Add the points from this transaction to the total points counter
	        totalPoints += points;
	    }

	    // Build a list of MonthlyReward objects from the monthlyPoints map (converts map entries into MonthlyReward objects)
	    List<MonthlyReward> monthlyRewards = buildMonthlyRewards(monthlyPoints);

	    // Convert the list of Transaction objects into TransactionDto objects
	    // This step is important to map internal entities (Transaction) to the data format you want to expose in the API
	    List<TransactionDto> transactionDtos = customerTxns.stream()
	        // For each transaction, create a TransactionDto object with the necessary fields
	        .map(txn -> TransactionDto.builder()
	                .id(txn.getId())                // Set the ID of the transaction
	                .customerId(txn.getCustomerId()) // Set the customer ID for the transaction
	                .amount(txn.getAmount())         // Set the transaction amount
	                .transactionDate(txn.getTransactionDate()) // Set the transaction date
	                .build())                        // Build the TransactionDto object
	        // Collect all the TransactionDto objects into a list
	        .collect(Collectors.toList());

	    // Return a new RewardResponse object with all the calculated data (monthly rewards, total points, and transactions)
	    return new RewardResponse(customerId, monthlyRewards, totalPoints);
	}
	/**
	 * Step 1 : Round down to the nearest whole number Calculates reward points
	 * based on the transaction amount using the following rules: - No points for
	 * amounts $50 or less. - 1 point for every dollar spent over $50 up to $100. -
	 * 2 points for every dollar spent over $100.
	 *
	 * Examples: - $120 → (20 * 2) + 50 = 90 points - $80 → (80 - 50) = 30 points -
	 * $45 → 0 points
	 *
	 * @param amount The transaction amount
	 * @return The calculated reward points
	 */
	public int calculatePoints(Double amount) {
	    if (amount == null) {
	        throw new RewardCalculationException("Transaction amount cannot be null.");
	    }

	    int roundedAmount = (int) Math.floor(amount);
	    int points = 0;

	    if (roundedAmount > 100) {
	        points += (roundedAmount - 100) * 2;
	        points += 50;
	    } else if (roundedAmount > 50) {
	        points += roundedAmount - 50;
	    }

	    return points;
	}
	
	private void validateTransaction(Transaction txn) {
	    if (txn.getTransactionDate() != null && txn.getTransactionDate().isAfter(LocalDate.now())) {
	        throw new RewardCalculationException("Transaction date cannot be in the future: " + txn.getTransactionDate());
	    }
	}

	/**
	 * Calculates reward details for a specific customer within a given date range.
	 * 
	 * Step: 1. If startDate or endDate is not provided, defaults to the last 3
	 * months. Step: 2. Filters transactions to include only those within the
	 * specified date range. Step: 3. Calculates reward points for each transaction
	 * based on the amount: - 2 points per dollar over $100 - 1 point per dollar
	 * over $50 (up to $100) Step: 4. Groups points by month and calculates the
	 * total. 6. Builds a response including: - Customer ID - Start and end date -
	 * Monthly reward breakdown ("MONTH YYYY" format) - Total reward points - List
	 * of filtered transactions
	 *
	 * @param customerId ID of the customer
	 * @param startDate  Start of the reward calculation period (nullable)
	 * @param endDate    End of the reward calculation period (nullable)
	 * @return RewardDetailsResponse with full reward summary
	 */
	public RewardDetailsResponse calculateRewardsForCustomer(String customerId, LocalDate startDate, LocalDate endDate) {

	    try {
	        // Retrieve transactions
	        Optional<List<Transaction>> customerTransactions = getTransactionsForCustomer(customerId, startDate, endDate);

	        // Resolve the date range
	        LocalDateRange dateRange = resolveDateRange(startDate, endDate);

	        // Filter transactions by date
	        List<Transaction> filteredTransactions = filterTransactionsByDate(
	                customerTransactions.orElse(Collections.emptyList()), dateRange);

	        // Calculate monthly points
	        Map<YearMonth, Integer> monthlyPoints = calculateMonthlyPoints(filteredTransactions);

	        // Build monthly reward details
	        List<MonthlyReward> monthlyRewards = buildMonthlyRewards(monthlyPoints);

	        // Calculate total points
	        int totalPoints = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();

	        // Convert Transaction to TransactionDto
	        List<TransactionDto> transactionDtos = filteredTransactions.stream()
	                .map(txn -> TransactionDto.builder()
	                        .id(txn.getId())
	                        .customerId(txn.getCustomerId())
	                        .amount(txn.getAmount())
	                        .transactionDate(txn.getTransactionDate())
	                        .build())
	                .collect(Collectors.toList());

	        // Return RewardDetailsResponse with TransactionDto list
	        return new RewardDetailsResponse(customerId, dateRange.start(), dateRange.end(), monthlyRewards,
	                totalPoints, transactionDtos);
	    } catch (Exception e) {
	        log.error(ClassUtil.LOG_PATTERN_RESPONSE, e.getLocalizedMessage());
	        throw new RewardCalculationException("Error calculating rewards for customer: " + customerId, e);
	    }
	}

	private Optional<List<Transaction>> getTransactionsForCustomer(String customerId, LocalDate startDate, LocalDate endDate) {
		 // If startDate is null, default to 3 months ago from today
	    if (startDate == null) {
	        startDate = LocalDate.now().minusMonths(3);
	    }

	    // If endDate is null, and any future date
	    if (endDate != null && endDate.isAfter(LocalDate.now())) {
	        throw new InvalidDateRangeException("Transaction end date cannot be in the future.");
	    }

		List<Transaction> transactions = repository.findByCustomerIdAndTransactionDateRange(customerId, startDate, endDate);
		return Optional.of(transactions).filter(list -> !list.isEmpty());
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
			validateTransaction(txn);
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