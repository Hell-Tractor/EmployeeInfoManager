package com.employeeinfomanager.aop;

import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DaoAspect {
    private final Logger logger = LoggerFactory.getLogger(DaoAspect.class);

    @Around("com.employeeinfomanager.aop.CommonPointCuts.daos()")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        Object obj = null;

        MethodSignature ms = (MethodSignature) jp.getSignature();
        Object target = jp.getTarget();

        try {
            obj = jp.proceed();
            logger.debug("doAround: obj = {}, method = {}", target, ms.getName());
        } catch (BusinessException e) {
            throw e; // handled in ControllerAspect
        } catch (Exception e) {
            logger.error("doAround: obj = {}, method = {}, e = {}", target, ms.getName(), e.toString());
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return obj;
    }
}
