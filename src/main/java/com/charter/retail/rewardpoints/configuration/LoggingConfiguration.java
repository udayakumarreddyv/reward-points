package com.charter.retail.rewardpoints.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingConfiguration {

    /** Handle to the log file */
    public static final Logger logger = LoggerFactory.getLogger(LoggingConfiguration.class);

    public LoggingConfiguration() {
    }

    @AfterReturning("execution( * com.charter.retail..*.*(..))")
    public void logMethodAccessAfter(JoinPoint joinPoint) {
        logger.info("Completed: " + joinPoint.getTarget().getClass() + ":" + joinPoint.getSignature().getName());
    }

    @Before("execution( * com.charter.retail..*.*(..))")
    public void logMethodAccessBefore(JoinPoint joinPoint) {
        logger.info("Starting: " + joinPoint.getTarget().getClass() + ":" + joinPoint.getSignature().getName());
    }
}