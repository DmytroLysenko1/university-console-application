package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.slf4j.MDC.clear;
import static org.slf4j.MDC.put;


@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final String REQUEST_ID = "requestId";

    @Before("execution(* org.example.service.impl.*ServiceImpl.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        put(REQUEST_ID, generateRequestId());
        logger.info("Entering method: {} with arguments: {}",
                joinPoint.getSignature().getName(),
                logArguments(joinPoint.getArgs()));
    }

    @After("execution(* org.example.service.impl.*ServiceImpl.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.debug("Exiting method: {}", joinPoint.getSignature().getName());
        clear();
    }

    @AfterThrowing(value = "execution(* org.example.service.impl.*ServiceImpl.*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            logger.warn("Warning in method: {} with cause: {}",
                    joinPoint.getSignature().getName(),
                    ex.getMessage());
        } else {
            logger.error("Critical error in method: {} with cause: {}",
                    joinPoint.getSignature().getName(),
                    ex.getMessage(),
                    ex);
        }
    }

    @Around("execution(* org.example.service.impl.*ServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.info("Method: {} executed in {} ms with result: {}",
                    joinPoint.getSignature().getName(),
                    timeTaken,
                    logResult(result));
            return result;
        } catch (Throwable ex) {
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.error("Method: {} executed in {} ms with exception: {}",
                    joinPoint.getSignature().getName(),
                    timeTaken,
                    ex.getMessage(),
                    ex);
            throw ex;
        } finally {
            clear();
        }
    }

    @Around("execution(* org.example.repositrory.*Repository.*(..))")
    public Object logRepositoryCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.debug("Database query executed: {} in {} ms",
                    joinPoint.getSignature().getName(),
                    timeTaken);
            return result;
        } catch (Throwable ex) {
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.error("Database query failed: {} executed in {} ms with exception: {}",
                    joinPoint.getSignature().getName(),
                    timeTaken,
                    ex.getMessage(),
                    ex);
            throw ex;
        }
    }

    private String generateRequestId() {
        return "REQ-" + System.currentTimeMillis();
    }

    private String logArguments(Object[] args) {
        return args != null ? String.join(", ", (CharSequence[]) args) : "No arguments";
    }

    private String logResult(Object result) {
        return result != null ? result.toString() : "No result";
    }
}