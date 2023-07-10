package com.terminal.logAspect;

import com.terminal.dtos.TerminalResponseDto;
import com.terminal.entities.Department;
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
import java.util.Set;

@Aspect
@Log4j2
@Component
public class TerminalLogAspect {

    private final Logger logger = LogManager.getLogger(TerminalLogAspect.class);

    @Pointcut("execution(* com.terminal.services.RegisterTerminalsService.*(..))") // return type, class, method with any parameters
    public void RegisterTerminalsPointCut() {}

    @Pointcut("execution(* com.terminal.services.ListTerminalsService.*(..))") // return type, class, method with any parameters
    public void ListTerminalsPointCut() {}

    @Around("RegisterTerminalsPointCut()")
    public Object registerTerminals(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

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

        if(object instanceof Set<?> && ((Set<?>) object).stream().allMatch(element -> element instanceof Department)) {
            Set<Department> departmentSet = (Set<Department>) object;

            for(Department department: departmentSet) {
                logger.info("Saved department is: " + department);
            }
        }

        logger.info(proceedingJoinPoint.getSignature() + " terminates");

        return object;
    }

    @Around("ListTerminalsPointCut()")
    public Object listTerminals(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String methodName = proceedingJoinPoint.getSignature().getName();
        logger.info(methodName + " begins");

        Object[] args = proceedingJoinPoint.getArgs();
        logger.info(methodName + "'s arguments are " + Arrays.toString(args));

        Object object = proceedingJoinPoint.proceed();

        if(object instanceof Page<?> && ((Page<?>) object).stream().allMatch(element -> element instanceof TerminalResponseDto)) {
            Page<TerminalResponseDto> terminalResponseDtos = (Page<TerminalResponseDto>) object;

            for(TerminalResponseDto terminalResponseDto: terminalResponseDtos) {
                logger.info("Searched page's content: " + terminalResponseDto.toString());
            }
        }

        logger.info(proceedingJoinPoint.getSignature() + " terminates");

        return object;
    }

}
