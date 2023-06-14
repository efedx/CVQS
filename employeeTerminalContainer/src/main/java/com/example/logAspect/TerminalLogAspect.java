package com.example.logAspect;

import com.example.dto.JwtDto;
import com.example.dto.TerminalResponseDto;
import com.example.model.Department;
import com.example.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Log4j2
@Component
public class TerminalLogAspect {

    private final Logger logger = LogManager.getLogger(TerminalLogAspect.class);

    @Pointcut("execution(* com.example.services.RegisterTerminalsService.*(..))") // return type, class, method with any parameters
    public void RegisterTerminalsPointCut() {}

    @Pointcut("execution(* com.example.services.ListTerminalsService.*(..))") // return type, class, method with any parameters
    public void ListTerminalsPointCut() {}

    @Around("RegisterTerminalsPointCut()")
    public Object registerTerminals(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        logger.info(proceedingJoinPoint.getSignature() + " begins");

        Object object = proceedingJoinPoint.proceed();

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

        logger.info(proceedingJoinPoint.getSignature() + " begins");

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
