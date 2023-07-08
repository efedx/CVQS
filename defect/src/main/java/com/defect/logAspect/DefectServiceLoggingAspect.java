package com.defect.logAspect;

import com.defect.model.Defect;
import com.defect.model.Vehicle;
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

@Aspect
@Log4j2
@Component
public class DefectServiceLoggingAspect {

    private final Logger logger = LogManager.getLogger(DefectServiceLoggingAspect.class);

    @Pointcut("execution(* com.security.services.*.*(..))") // return type, class, method with any parameters
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
            logger.error("An error occurred: " + throwable.toString(), throwable); //.getMessage() spring trace
            throw throwable; // Rethrow the exception after logging
        }

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
