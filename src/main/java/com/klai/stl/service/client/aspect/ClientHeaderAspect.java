package com.klai.stl.service.client.aspect;

import static java.util.Arrays.stream;

import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.client.exception.InvalidTokenException;
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

    private final CompanyRepository companyRepository;

    public ClientHeaderAspect(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Around("methodAnnotatedWithValidHeaderAnnotation()")
    public Object validateHeader(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        CodeSignature codeSignature = (CodeSignature) proceedingJoinPoint.getSignature();

        stream(codeSignature.getParameterNames()).filter(byToken()).findFirst().orElseThrow(RuntimeException::new);

        companyRepository.findByToken(proceedingJoinPoint.getArgs()[0].toString()).orElseThrow(InvalidTokenException::new);

        return proceedingJoinPoint.proceed();
    }

    @Pointcut(
        value = "@within(com.klai.stl.service.client.annotation.ValidClientHeader) || @annotation(com.klai.stl.service.client.annotation.ValidClientHeader)"
    )
    public void methodAnnotatedWithValidHeaderAnnotation() {}

    private Predicate<String> byToken() {
        return methodParameter -> methodParameter.equalsIgnoreCase("token");
    }
}
