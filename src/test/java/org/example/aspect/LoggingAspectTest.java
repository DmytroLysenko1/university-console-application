package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.example.annotations.Loggable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.slf4j.MDC.get;
import static org.slf4j.MDC.put;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @InjectMocks
    private LoggingAspect loggingAspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Loggable loggable;

    @Mock
    private Signature signature;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logBefore_withArguments_loggingSuccess() {
        when(this.joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});
        when(this.loggable.logArguments()).thenReturn(true);
        when(this.joinPoint.getSignature()).thenReturn(this.signature);
        when(this.signature.getName()).thenReturn("testMethod");

        this.loggingAspect.logBefore(this.joinPoint, this.loggable);

        assertNotNull(get("requestId"));
        verify(this.joinPoint, times(1)).getSignature();
        verify(this.joinPoint, times(1)).getArgs();
    }

    @Test
    void logBefore_withoutArguments_loggingSuccess() {
        when(this.loggable.logArguments()).thenReturn(false);
        when(this.joinPoint.getSignature()).thenReturn(this.signature);

        this.loggingAspect.logBefore(this.joinPoint, this.loggable);

        assertNotNull(get("requestId"));
        verify(this.joinPoint, times(1)).getSignature();
    }

    @Test
    void logAfter_methodExit_loggingSuccess() {
        when(this.joinPoint.getSignature()).thenReturn(this.signature);
        when(this.signature.getName()).thenReturn("testMethod");

        put("requestId", "REQ-12345");

        this.loggingAspect.logAfter(this.joinPoint);

        verify(this.joinPoint, times(1)).getSignature();
        assertNull(get("requestId"));
    }

    @Test
    void logAfterThrowing_withException_loggingSuccess() {
        when(this.joinPoint.getSignature()).thenReturn(this.signature);
        when(this.signature.getName()).thenReturn("testMethod");
        when(this.loggable.logException()).thenReturn(true);
        Throwable exception = new RuntimeException("Test Exception");

        put("requestId", "REQ-12345");

        loggingAspect.logAfterThrowing(this.joinPoint, exception, this.loggable);

        verify(this.joinPoint, times(1)).getSignature();
    }

    @Test
    void logAround_methodExecution_loggingSuccess() throws Throwable {
        when(this.loggable.logReturnValue()).thenReturn(true);
        when(this.proceedingJoinPoint.proceed()).thenReturn("Success");
        when(this.proceedingJoinPoint.getSignature()).thenReturn(this.signature);
        when(this.signature.getName()).thenReturn("testMethod");

        put("requestId", "REQ-12345");

        Object result = this.loggingAspect.logAround(this.proceedingJoinPoint, this.loggable);

        verify(this.proceedingJoinPoint, times(1)).proceed();
        assertEquals("Success", result);
        assertNull(get("requestId"));
    }

    @Test
    void logAround_methodExecutionWithException_loggingFailure() throws Throwable {
        when(this.proceedingJoinPoint.getSignature()).thenReturn(this.signature);
        when(this.signature.getName()).thenReturn("testMethod");
        when(this.proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Test Exception"));

        put("requestId", "REQ-12345");

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> loggingAspect.logAround(this.proceedingJoinPoint, this.loggable));

        assertEquals("Test Exception", thrown.getMessage());
        verify(this.proceedingJoinPoint, times(1)).proceed();
        assertNull(get("requestId"));
    }

    @Test
    void logResult_withLongResult_truncated() {
        String longResult = "A".repeat(250);
        String result = this.loggingAspect.logResult(longResult);
        assertTrue(result.length() <= 203);
        assertTrue(result.endsWith("..."));
    }

    @Test
    void generateRequestId_validInvocation_returnsRequestId() {
        String requestId = this.loggingAspect.generateRequestId();
        assertTrue(requestId.startsWith("REQ-"));
    }
}

