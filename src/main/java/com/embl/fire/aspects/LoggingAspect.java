package com.embl.fire.aspects;

import com.embl.fire.logging.ILogger;
import com.embl.fire.logging.ILoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final ILogger logger = ILoggerFactory.getLogger(this.getClass());
    ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut(value = "execution(* com.embl.fire.api.PersonsApiDelegateImpl.*(..))")
    public void pointCutForAllApis() {

    }

    @Around(value = "pointCutForAllApis()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] methodArguments = joinPoint.getArgs();

        logger.info("Method invoked {}:{}()", className, methodName);


        try {
            logger.debug(() -> String.format("Entered: %s.%s() with argument[s] = %s",
                    joinPoint.getSignature().getDeclaringType(), methodName, Arrays.toString(methodArguments)));

            Object result = joinPoint.proceed();

            logger.debug(() -> getFormattedMessage(methodName, className, result));

            return result;

        } catch (Throwable ex) {
            logger.error("Exception occurred: " + ex.getMessage());
            throw ex;
        }
    }

    private String getFormattedMessage(String methodName, String className, Object result) {
        try {
            return String.format("%s, method: %s(), Response: %s",
                    className, methodName, objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return "Error while parsing the api response ";
        }
    }
}

