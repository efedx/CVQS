package com.example.loggingAspect;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;


@Aspect
@Log4j2
@Component
public class JwtValidationServiceLogging {

    private final Logger logger = LogManager.getLogger(JwtValidationServiceLogging.class);

    @Pointcut("execution(* com.example.services.JwtValidationService.*(..))") // return type, class, method with any parameters
    public void LoggingPointCut() {}

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
            // Log the error
            logger.error("An error occurred: " + throwable.toString()); //.getMessage()
            throw throwable; // Rethrow the exception after logging
        }

        if(object instanceof Boolean isValid) {
            if(isValid) {
                log.info("Response is: " + isValid + ". Token is valid");
            }
            else {
                log.info("Response is: " + isValid + ". Token is not valid");
            }
        }

        logger.info(proceedingJoinPoint.getSignature() + " terminates");

        return object;
    }

}
