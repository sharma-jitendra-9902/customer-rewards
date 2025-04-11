package com.customer.loyalty.program.utils;

public final class ClassUtil {

	private ClassUtil() {

	}


	public static final String LOG_PATTERN_REQUEST = "request={}";
	public static final String LOG_PATTERN_RESPONSE = "response={}";
	public static final String LOG_PATTERN_ERROR = "error={}";
	public static final String LOG_PATTERN_CUSTOMER_ID = "methodName={} customerid={}";
	public static final String LOG_PATTERN_METHOD_NAME = "methodName={}";

	public static final String ERROR = "ERROR";
	public static final String NO_RECORD_IN_DB = "No record in DB";
	public static final String SUCCESS = "SUCCESS";
	public static final String REWARDS_FETCHED_SUCCESFULLY = "Rewards fetched successfully";
	public static final String CUSTOMER_NOT_FOUND_IN_DB = "Customer not found in DB";
	public static final String CUSTOMER_DETAILS_FETCHED_SUCCESFULLY = "Customer details fetched successfully";
	public static final String CUSTOMERID_SHOULD_NOT_BE_EMPTY = "CustomerId is null or Empty";
	public static final String ALL_CUSTOMER_REWARDS = "get last 3 month and total rewards pointes of all customer endpoint";
}
