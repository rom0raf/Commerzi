package com.commerzi.commerziapi.security;

import com.commerzi.commerziapi.model.User;
import com.commerzi.commerziapi.service.IAuthentificationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class CommerziAuthenticatedAspect {

    @Autowired
    private IAuthentificationService authentificationService;

    @Around("@annotation(com.commerzi.commerziapi.security.CommerziAuthenticated)")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String session = request.getHeader("Session");

        User user = authentificationService.getUserBySession(session);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return joinPoint.proceed();
    }
}
