package com.example.logAspect;


import com.example.dto.JwtDto;
import com.example.model.Employee;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;


@Aspect
@Log4j2
@Component
public class LogAspect {

    private final Logger logger = LogManager.getLogger(LogAspect.class);

    @Pointcut("execution(* com.example.services.*.*(..))") // return type, class, method with any parameters
    public void LoggingPointCut() {}

//    @Before("LoggingPointCut()")
//    public void before(JoinPoint joinPoint) {
//        log.info("Before method invoked: "); //, joinPoint.getSignature()
//    }
//
//    @After("LoggingPointCut()")
//    public void after(JoinPoint joinPoint) {
//        log.info("After method invoked: "); //, joinPoint.getSignature()
//    }
//
//    @AfterReturning(value = "LoggingPointCut()", returning = "string")
//    public void afterReturning(JoinPoint joinPoint, String string) {
//        log.info("After returning invoked: " + string); //, joinPoint.getSignature()
//    }

//    @Around("LoggingPointCut()")
//    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//
//            log.info("Before method invoked" + proceedingJoinPoint.getSignature());
//
//            Object object = proceedingJoinPoint.proceed();
//
//            log.info("Before method invoked" + proceedingJoinPoint.getTarget());
//
//            if(object instanceof String) {
//                log.info("After method invoked" + object);
//            }
//            else if(object instanceof Employee) {
//                log.info("After method invoked " + object);
//            }
//            return object;
//    }

    @Around("LoggingPointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        logger.info("Register employee begins" + proceedingJoinPoint.getSignature());

        Object object = proceedingJoinPoint.proceed();

        if(object instanceof Set<?> && ((Set<?>) object).stream().allMatch(element -> element instanceof Employee)) {
            Set<Employee> employeeSet = (Set<Employee>) object;

            for(Employee employee: employeeSet) {
                logger.info("saved employee is: " + employee);
            }
            logger.info("Register employee terminates" + proceedingJoinPoint.getSignature());
        }

        else if(object instanceof JwtDto) {
            logger.info("After method invoked " + object);
        }



        return object;
    }

}
