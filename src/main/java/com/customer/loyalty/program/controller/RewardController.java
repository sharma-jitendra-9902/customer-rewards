package com.customer.loyalty.program.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.loyalty.program.dto.ApiResponse;
import com.customer.loyalty.program.dto.RewardDetailsResponse;
import com.customer.loyalty.program.dto.RewardResponse;
import com.customer.loyalty.program.entity.Transaction;
import com.customer.loyalty.program.service.RewardService;
import com.customer.loyalty.program.service.TransactionService;
import com.customer.loyalty.program.utils.ClassUtil;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class RewardController {

	private final RewardService rewardService;
	private final TransactionService transactionService;

	public RewardController(RewardService rewardService, TransactionService transactionService) {
		this.rewardService = rewardService;
		this.transactionService = transactionService;
	}

	@GetMapping("/rewards")
	public ResponseEntity<ApiResponse<List<RewardResponse>>> getAllCustomerRewards() {

		log.info(ClassUtil.LOG_PATTERN_METHOD_NAME, "getAllCustomerRewards()");

		List<RewardResponse> rewards = rewardService.calculateRewards();

		if (rewards.isEmpty()) {
			log.error(ClassUtil.LOG_PATTERN_REQUEST, ClassUtil.ALL_CUSTOMER_REWARDS);
			ApiResponse<List<RewardResponse>> response = new ApiResponse<>(ClassUtil.ERROR, ClassUtil.NO_RECORD_IN_DB,
					null);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} else {
			log.info(ClassUtil.LOG_PATTERN_RESPONSE, String.valueOf(rewards));
			ApiResponse<List<RewardResponse>> response = new ApiResponse<>(ClassUtil.SUCCESS,
					ClassUtil.REWARDS_FETCHED_SUCCESFULLY, rewards);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/customer/reward/{customerId}")
	public ResponseEntity<ApiResponse<RewardDetailsResponse>> getRewardsForCustomer(@PathVariable String customerId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		log.info(ClassUtil.LOG_PATTERN_CUSTOMER_ID, "getRewardsForCustomer()", customerId);

		if (customerId == null) {
			ApiResponse<RewardDetailsResponse> response = new ApiResponse<>(ClassUtil.ERROR,
					ClassUtil.CUSTOMERID_SHOULD_NOT_BE_EMPTY, null);
			log.error(ClassUtil.LOG_PATTERN_REQUEST, ClassUtil.CUSTOMERID_SHOULD_NOT_BE_EMPTY);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			RewardDetailsResponse rewardDetailsResponse = rewardService.calculateRewardsForCustomer(customerId,
					startDate, endDate);

			boolean isEmpty = rewardDetailsResponse.getMonthlyRewards().isEmpty();

			String message = isEmpty ? ClassUtil.CUSTOMER_NOT_FOUND_IN_DB
					: ClassUtil.CUSTOMER_DETAILS_FETCHED_SUCCESFULLY;
			String status = isEmpty ? ClassUtil.ERROR : ClassUtil.SUCCESS;

			ApiResponse<RewardDetailsResponse> response = new ApiResponse<>(status, message,
					isEmpty ? null : rewardDetailsResponse);

			if (isEmpty) {
				log.error(ClassUtil.LOG_PATTERN_REQUEST, message);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			} else {
				log.info(ClassUtil.LOG_PATTERN_RESPONSE, String.valueOf(response));
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

	}

	@GetMapping("/health")
	public ResponseEntity<String> health() {
		log.info(ClassUtil.LOG_PATTERN_REQUEST, "health endpoint");
		return new ResponseEntity<>(new Gson().fromJson("Success", String.class), HttpStatus.OK);
	}

	@PostMapping(path = "/store/customer/data", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
		log.info(ClassUtil.LOG_PATTERN_REQUEST, String.valueOf(transaction));
		Transaction savedTransaction = transactionService.insertTransaction(transaction);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
	}

}
