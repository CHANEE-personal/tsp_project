package com.tsp.new_tsp_front.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class TspCommonAop {

	/**
	 * <pre>
	 * 1. MethodName : cut
	 * 2. ClassName  : TspCommonAop.java
	 * 3. Comment    : com.tsp.new_tsp_front..api 이하 패키지의 모든 클래스 이하 모든 메서드에 적용
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 16.
	 * </pre>
	 *
	 */
	@Pointcut("execution(* com.tsp.new_tsp_front..api..*.*(..))")
	private void cut(){}

	/**
	 * <pre>
	 * 1. MethodName : beforeParameterLog
	 * 2. ClassName  : TspCommonAop.java
	 * 3. Comment    : Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 16.
	 * </pre>
	 *
	 */
	@Before("cut()")
	public void beforeParameterLog(JoinPoint joinPoint) {
		// 메서드 정보 받아오기
		Method method = getMethod(joinPoint);
		log.info("======= method name = {} =======", method.getName());

		// 파라미터 받아오기
		Object[] args = joinPoint.getArgs();
		if (args.length <= 0) log.info("no parameter");
		for (Object arg : args) {
			log.info("parameter type = {}", arg.getClass().getSimpleName());
			log.info("parameter value = {}", arg);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : afterReturnLog
	 * 2. ClassName  : TspCommonAop.java
	 * 3. Comment    : Poincut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 16.
	 * </pre>
	 *
	 */
	@AfterReturning(value = "cut()", returning = "returnObj")
	public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
		// 메서드 정보 받아오기
		Method method = getMethod(joinPoint);
		log.info("======= method name = {} =======", method.getName());

		if (returnObj != null) {
			log.info("return type = {}", returnObj.getClass().getSimpleName());
			log.info("return value = {}", returnObj);
		}
	}

	// JoinPoint로 메서드 정보 가져오기
	private Method getMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getMethod();
	}
}
