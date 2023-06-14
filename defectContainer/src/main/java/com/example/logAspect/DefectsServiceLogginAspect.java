package com.example.logAspect;

import com.example.model.Defect;
import com.example.model.Vehicle;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;


@Aspect
@Log4j2
@Component
public class DefectsServiceLogginAspect {

    private final Logger logger = LogManager.getLogger(DefectsServiceLogginAspect.class);

    @Pointcut("execution(* com.example.services.*.*(..))") // return type, class, method with any parameters
    public void LoggingPointCut() {}

    @Around("LoggingPointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String methodName = proceedingJoinPoint.getSignature().getName();
        logger.info(methodName + " begins");

        Object[] args = proceedingJoinPoint.getArgs();
        logger.info(methodName + "'s arguments are " + Arrays.toString(args));

        Object object = proceedingJoinPoint.proceed();

        if(object instanceof List<?> && ((List<?>) object).stream().allMatch(element -> element instanceof Vehicle)) {
            List<Vehicle> vehicleList = (List<Vehicle>) object;

            for(Vehicle vehicle: vehicleList) {
                logger.info("Saved vehicle is: " + vehicle.toString());
            }
        }

        else if(object instanceof Page<?> && ((Page<?>) object).stream().allMatch(element -> element instanceof Vehicle)) {
            Page<Vehicle> vehiclePage = (Page<Vehicle>) object;

            for(Vehicle vehicle: vehiclePage) {
                logger.info("Searched page's content: " + vehicle.toString());
            }
        }

        else if(object instanceof Page<?> && ((Page<?>) object).stream().allMatch(element -> element instanceof Defect)) {
            Page<Defect> defectPage = (Page<Defect>) object;

            for(Defect defect: defectPage) {
                logger.info("Searched page's content: " + defect.toString());
            }
        }

        else if(object instanceof byte[] imageByte) {
            logger.info("Image's size is " + imageByte.length);
        }

        logger.info(proceedingJoinPoint.getSignature() + " terminates");

        return object;
    }

}
