package com.notification.logAspect;


import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Aspect
@Log4j2
@Component
public class NotificationServiceLogAspect {

    private final Logger logger = LogManager.getLogger(NotificationServiceLogAspect.class);

    @Pointcut("execution(* com.notification.services.NotificationService.*(..))")
    public void LoggingPointCut() {};

    @Around("LoggingPointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String methodName = proceedingJoinPoint.getSignature().getName();
        logger.info(methodName + " begins");

        Object[] args = proceedingJoinPoint.getArgs();
        logger.info(methodName + "'s arguments are " + Arrays.toString(args));

        Object object;

        try {
            object = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {

            logger.error("An error occurred: " + throwable.toString());
            throw throwable;
        }

        logger.info(proceedingJoinPoint.getSignature() + " terminates");

        return object;
    }

}
