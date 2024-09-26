package com.employeeinfomanager.aop;

import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.common.ReturnObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@Order(10)
public class ControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Value("${EmployeeInfoManager.page-size.max}")
    private int MAX_PAGE_SIZE;
    @Value("${EmployeeInfoManager.page-size.default}")
    private int DEFAULT_PAGE_SIZE;

    @Value("${EmployeeInfoManager.time.begin}")
    private LocalDateTime BEGIN_TIME;
    @Value("${EmployeeInfoManager.time.end}")
    private LocalDateTime END_TIME;

    @Around("com.employeeinfomanager.aop.CommonPointCuts.controllers()")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        ReturnObject retVal = null;

        MethodSignature ms = (MethodSignature) jp.getSignature();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        String[] paramNames = ms.getParameterNames();
        logger.debug("doAround: method = {}, paramNames = {}", ms.getName(), paramNames);
        Object[] args = jp.getArgs();
        try {
            Object[] newArgs = checkPageSizeAndTimeLimit(request, paramNames, args);
            Object obj = jp.proceed(newArgs);
            retVal = (ReturnObject) obj;
        } catch (BusinessException exception) {
            logger.info("doAround: BussinessException, code = {}, message = {}", exception.getCode(), exception.getMessage());
            retVal = new ReturnObject(exception.getCode(), exception.getMessage());
        }

        ReturnNo code = retVal.getCode();
        logger.debug("doAround: jp = {}, code = {}", jp.getSignature().getName(), code);
        response.setStatus(code.getHttpStatusCode());
        response.setContentType("application/json;charset=UTF-8");

        return retVal;
    }

    private Object[] checkPageSizeAndTimeLimit(HttpServletRequest request, String[] paramNames, Object[] args) {
        int page = 1, pageSize = DEFAULT_PAGE_SIZE;

        if (request != null) {
            String pageString = request.getParameter("page");
            String pageSizeString = request.getParameter("pageSize");
            if (null != pageString  && !pageString.isEmpty() && pageString.matches("\\d+")) {
                page = Integer.parseInt(pageString);
                if (page <= 0) {
                    page = 1;
                }
            }

            if (null !=pageSizeString  && !pageSizeString.isEmpty() && pageSizeString.matches("\\d+")) {
                pageSize = Integer.parseInt(pageSizeString);
                if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
                    pageSize = DEFAULT_PAGE_SIZE;
                }
            }
        }


        for (int i = 0; i < paramNames.length; i++) {
            logger.debug("checkPageTimeLimit: paramNames[{}] = {}", i, paramNames[i]);
            if (paramNames[i].equals("page")) {
                logger.debug("checkPageTimeLimit: set {} to {}",paramNames[i], page);
                args[i] = page;
                continue;
            }

            if (paramNames[i].equals("pageSize")) {
                logger.debug("checkPageTimeLimit: set {} to {}",paramNames[i], pageSize);
                args[i] = pageSize;
                continue;
            }

            if (paramNames[i].equals("beginTime") && (args[i] == null)){
                logger.debug("checkPageTimeLimit: set {} to {}",paramNames[i], BEGIN_TIME);
                args[i] = BEGIN_TIME;
                continue;
            }

            if (paramNames[i].equals("endTime") && (args[i] == null)){
                logger.debug("checkPageTimeLimit: set {} to {}",paramNames[i], END_TIME);
                args[i] = END_TIME;
            }
        }
        return args;
    }
}
