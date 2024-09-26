package com.employeeinfomanager.aop;

import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.JwtHelper;
import com.employeeinfomanager.common.ReturnNo;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(20)
public class AuditAspect {
    private final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @Around("com.employeeinfomanager.aop.CommonPointCuts.auditAnnotation()")
    public Object aroundAudit(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(JwtHelper.LOGIN_TOKEN_KEY);
        UserToken decryptedToken = this.decryptToken(token);

        this.checkAuditLevel(method, decryptedToken);

        Object[] args = pjp.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        putMethodParameter(decryptedToken, args, annotations);

        return pjp.proceed(args);
    }

    private void putMethodParameter(UserToken token, Object[] args, Annotation[][] annotations) {
        for (int i = 0; i < annotations.length; ++i) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType().equals(LoginUser.class)) {
                    args[i] = token.getUserId();
                    break;
                }
            }
        }
    }

    private void checkAuditLevel(Method method, UserToken decryptedToken) throws BusinessException {
        Audit auditAnnotation = method.getAnnotation(Audit.class);
        if (auditAnnotation == null) {
            logger.debug("check audit level: no audit require");
            return;
        }
        AuditLevel requireAuditLevel = auditAnnotation.value();
        if (requireAuditLevel.ordinal() > decryptedToken.getUserLevel().ordinal())
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT);
    }

    private UserToken decryptToken(String token) throws BusinessException {
        if (token == null || token.isEmpty()) {
            logger.debug("decrypt token: no token");
            throw new BusinessException(ReturnNo.AUTH_LOGIN_REQUIRED);
        }

        UserToken result = new JwtHelper().verifyTokenAndGetClaims(token);
        if (result == null) {
            logger.debug("decrypt token: invalid token");
            throw new BusinessException(ReturnNo.AUTH_INVALID_TOKEN);
        }

        if (result.getUserId() == null) {
            logger.debug("decrypt token: no username in token");
            throw new BusinessException(ReturnNo.AUTH_LOGIN_REQUIRED);
        }

        return result;
    }
}
