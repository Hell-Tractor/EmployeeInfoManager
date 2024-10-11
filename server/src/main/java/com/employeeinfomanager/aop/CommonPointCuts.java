package com.employeeinfomanager.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonPointCuts {
    @Pointcut("execution(public com.employeeinfomanager.common.ReturnObject com.employeeinfomanager.controller..*.*(..))")
    public void controllers() {}

    @Pointcut("execution(public * com.employeeinfomanager..dao..*.*(..))")
    public void daos() {}

    @Pointcut("@annotation(com.employeeinfomanager.aop.Audit)")
    public void auditAnnotation() {}
}
