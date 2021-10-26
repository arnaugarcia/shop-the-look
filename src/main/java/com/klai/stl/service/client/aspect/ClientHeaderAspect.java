package com.klai.stl.service.client.aspect;

import com.klai.stl.service.CompanyService;
import java.util.Arrays;
import java.util.function.Predicate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ClientHeaderAspect {

    private final CompanyService companyService;

    public ClientHeaderAspect(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Around("methodAnnotatedWithValidHeaderAnnotation()")
    public Object validateHeader(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        CodeSignature codeSignature = (CodeSignature) proceedingJoinPoint.getSignature();
        final String token = Arrays
            .stream(codeSignature.getParameterNames())
            .filter(byParamName("token"))
            .findFirst()
            .orElseThrow(RuntimeException::new);
        companyService.findByToken(proceedingJoinPoint.getArgs()[0].toString());
        return proceedingJoinPoint.proceed();
    }

    private Predicate<String> byParamName(String paramName) {
        return methodParameter -> methodParameter.equalsIgnoreCase(paramName);
    }

    @Pointcut(
        value = "@within(com.klai.stl.service.client.annotation.ValidClientHeader) || @annotation(com.klai.stl.service.client.annotation.ValidClientHeader)"
    )
    public void methodAnnotatedWithValidHeaderAnnotation() {}
}
