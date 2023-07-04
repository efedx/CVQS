//package com.employee.logAspect;
//
//import lombok.extern.log4j.Log4j2;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.Set;
//
//@Aspect
//@Log4j2
//@Component
//public class SecurityContainerLogAspect {
//
////    private final Logger logger = LogManager.getLogger(SecurityContainerLogAspect.class);
////
////    @Pointcut("execution(* com.emmployee.services.SecurityContainerService.*(..))") // return type, class, method with any parameters
////    public void SecurityContainerPointCut() {}
////
////    @Around("SecurityContainerPointCut()")
////    public Object SecurityContainer(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
////
////        String methodName = proceedingJoinPoint.getSignature().getName();
////        logger.info(methodName + " begins");
////
////        Object[] args = proceedingJoinPoint.getArgs();
////        logger.info(methodName + "'s arguments are " + Arrays.toString(args));
////
////        Object object;
////
////        try {
////            object = proceedingJoinPoint.proceed();
////        } catch (Throwable throwable) {
////            // Log the error
////            logger.error("An error occurred: " + throwable.toString()); //.getMessage()
////            throw throwable; // Rethrow the exception after logging
////        }
////
//////        if(object instanceof ResponseEntity<?>) &&  {
//////            Set<Department> departmentSet = (Set<Department>) object;
//////
//////
//////        }
////
////        logger.info(proceedingJoinPoint.getSignature() + " terminates");
////
////        return object;
////    }
//
//}
