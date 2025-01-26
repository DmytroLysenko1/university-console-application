package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.example.annotations.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.slf4j.MDC.*;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final String REQUEST_ID = "requestId";

    @Before("@annotation(loggable)")
    public void logBefore(JoinPoint joinPoint, Loggable loggable) {
        String requestId = generateRequestId();
        put(REQUEST_ID, requestId);
        if (loggable.logArguments()) {
            logger.info("Entering method: {} with arguments: {}, Request ID: {}",
                    joinPoint.getSignature().getName(),
                    logArguments(joinPoint.getArgs()),
                    requestId);
        } else {
            logger.info("Entering method: {}, Request ID: {}",
                    joinPoint.getSignature().getName(),
                    requestId);
        }
    }

    @After("@annotation(org.example.annotations.Loggable)")
    public void logAfter(JoinPoint joinPoint) {
        String requestId = get(REQUEST_ID);
        logger.debug("Exiting method: {} with Request ID: {}", joinPoint.getSignature().getName(), requestId);
        clear();
    }

    @AfterThrowing(value = "@annotation(loggable)", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex, Loggable loggable) {
        String requestId = get(REQUEST_ID);
        if (loggable.logException()) {
            logger.error("Exception in method: {} with cause: {}. Request ID: {}",
                    joinPoint.getSignature().getName(),
                    ex.getMessage(),
                    requestId, ex);
        }
    }

    @Around("@annotation(loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        String requestId = get(REQUEST_ID);
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - startTime;
            if (loggable.logReturnValue()) {
                logger.info("Method: {} executed in {} ms with result: {}. Request ID: {}",
                        joinPoint.getSignature().getName(),
                        timeTaken,
                        logResult(result),
                        requestId);
            } else {
                logger.info("Method: {} executed in {} ms. Request ID: {}",
                        joinPoint.getSignature().getName(),
                        timeTaken,
                        requestId);
            }
            return result;
        } catch (Throwable ex) {
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.error("Method: {} executed in {} ms with exception: {}. Request ID: {}",
                    joinPoint.getSignature().getName(),
                    timeTaken,
                    ex.getMessage(),
                    requestId, ex);
            throw ex;
        } finally {
            clear();
        }
    }

    private String generateRequestId() {
        return "REQ-" + System.currentTimeMillis();
    }

    private String logArguments(Object[] args) {
        return args != null ? Arrays.stream(args)
                .map(Object::toString)
                .limit(5)
                .collect(Collectors.joining(", ")) : "No arguments";
    }

    private String logResult(Object result) {
        if (result != null) {
            String resultStr = result.toString();
            return resultStr.length() > 200 ? resultStr.substring(0, 200) + "..." : resultStr;
        }
        return "No result";
    }
}