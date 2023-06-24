package com.example.logAspect;


import com.example.dto.JwtDto;
import com.example.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
public class UserManagementServiceLogAspect {

    private final Logger logger = LogManager.getLogger(UserManagementServiceLogAspect.class);

    @Pointcut("execution(* com.example.services.UserManagementService.*(..))") // return type, class, method with any parameters
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
            logger.error("An error occurred: " + throwable.toString());
            throw throwable; // Rethrow the exception after logging
        }

        if(object instanceof JwtDto jwtDto) {
            String username = jwtDto.getUsername();
            String token = jwtDto.getToken();
            logger.info("Username with " + username + "'s token is: " + token);
        }

        else if(object instanceof Set<?> && ((Set<?>) object).stream().allMatch(element -> element instanceof Employee)) {
            Set<Employee> employeeSet = (Set<Employee>) object;

            for(Employee employee: employeeSet) {
                logger.info("Saved employee is: " + employee);
            }
        }


        else if(object instanceof Long id) {
            logger.info("Employee with id " + id + "deleted");
        }

        else if(object instanceof Employee employee) {
            logger.info("Id with " + employee.getId() + " updated to " + employee.toString());
        }
        logger.info(proceedingJoinPoint.getSignature() + " terminates");

        return object;
    }
}
