package com.customer.loyalty.program.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.customer.loyalty.program.dto.ApiResponse;
import com.customer.loyalty.program.dto.MonthlyReward;
import com.customer.loyalty.program.dto.RewardDetailsResponse;
import com.customer.loyalty.program.dto.RewardResponse;
import com.customer.loyalty.program.service.RewardService;
import com.customer.loyalty.program.utils.ClassUtil;

@ExtendWith(MockitoExtension.class)
class RewardControllerTest {

	@InjectMocks
	private RewardController rewardController;

	@Mock
	private RewardService rewardService;

	@Test
	void testGetAllCustomerRewards_WhenRewardsExist_ReturnsOk() {
		List<RewardResponse> mockRewards = List.of(new RewardResponse(/* add fields as needed */));
		when(rewardService.calculateRewards()).thenReturn(mockRewards);

		ResponseEntity<ApiResponse<List<RewardResponse>>> responseEntity = rewardController.getAllCustomerRewards();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(ClassUtil.SUCCESS, responseEntity.getBody().getStatus());
		assertEquals(ClassUtil.REWARDS_FETCHED_SUCCESFULLY, responseEntity.getBody().getMessage());
		assertEquals(mockRewards, responseEntity.getBody().getData());
	}

	@Test
	void testGetAllCustomerRewards_WhenNoRewardsExist_ReturnsNotFound() {
		when(rewardService.calculateRewards()).thenReturn(Collections.emptyList());

		ResponseEntity<ApiResponse<List<RewardResponse>>> responseEntity = rewardController.getAllCustomerRewards();

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(ClassUtil.ERROR, responseEntity.getBody().getStatus());
		assertEquals(ClassUtil.NO_RECORD_IN_DB, responseEntity.getBody().getMessage());
		assertNull(responseEntity.getBody().getData());
	}

	@Test
	void testGetRewardsForCustomer_WhenRewardsExist_ReturnsOk() {
		String customerId = "cust123";
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate endDate = LocalDate.of(2024, 3, 31);

		RewardDetailsResponse mockResponse = new RewardDetailsResponse();
		mockResponse.setMonthlyRewards(List.of(new MonthlyReward(/* fields here if needed */)));

		when(rewardService.calculateRewardsForCustomer(customerId, startDate, endDate)).thenReturn(mockResponse);

		ResponseEntity<ApiResponse<RewardDetailsResponse>> responseEntity = rewardController
				.getRewardsForCustomer(customerId, startDate, endDate);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(ClassUtil.SUCCESS, responseEntity.getBody().getStatus());
		assertEquals(ClassUtil.CUSTOMER_DETAILS_FETCHED_SUCCESFULLY, responseEntity.getBody().getMessage());
		assertEquals(mockResponse, responseEntity.getBody().getData());
	}

	@Test
	void testGetRewardsForCustomer_WhenNoRewardsExist_ReturnsNotFound() {
		String customerId = "cust999";
		LocalDate startDate = null;
		LocalDate endDate = null;

		RewardDetailsResponse emptyResponse = new RewardDetailsResponse();
		emptyResponse.setMonthlyRewards(Collections.emptyList());

		when(rewardService.calculateRewardsForCustomer(customerId, null, null)).thenReturn(emptyResponse);

		ResponseEntity<ApiResponse<RewardDetailsResponse>> responseEntity = rewardController
				.getRewardsForCustomer(customerId, null, null);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(ClassUtil.ERROR, responseEntity.getBody().getStatus());
		assertEquals(ClassUtil.CUSTOMER_NOT_FOUND_IN_DB, responseEntity.getBody().getMessage());
		assertNull(responseEntity.getBody().getData());
	}
}
