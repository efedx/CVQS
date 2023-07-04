package com.security.loggingAspect;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Log4j2
@Component
public class UserManagementSecurityLogging {

    private final Logger logger = LogManager.getLogger(UserManagementSecurityLogging.class);

    @Pointcut("execution(* com.security.services.LoginService.*(..))") // return type, class, method with any parameters
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

            logger.error("An error occurred: " + throwable.toString());
            throw throwable;
        }

        if(object instanceof String token) {
            logger.info("Token is: " + token);
        }

        logger.info(proceedingJoinPoint.getSignature() + " terminates");

        return object;
    }

}
