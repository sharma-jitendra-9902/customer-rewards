package com.customer.loyalty.program.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class AuditLogAop {

	@Around("execution(* com.customer.loyalty.program.controller.RewardController.*(..))")
	public Object logControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object proceed = joinPoint.proceed();
		long executionTime = System.currentTimeMillis() - start;
		log.info(joinPoint.getSignature() + " executed in " + executionTime + "ms");
		return proceed;
	}

}
