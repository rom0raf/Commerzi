package com.commerzi.commerziapi.security;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.service.interfaces.IAuthentificationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Aspect that handles authentication verification for methods annotated with {@link CommerziAuthenticated}.
 * <p>
 * This class is responsible for checking if the user is authenticated before executing methods annotated with {@code @CommerziAuthenticated}.
 * If the user is not authenticated, it returns an HTTP 401 (Unauthorized) response. If the user is authenticated,
 * it allows the normal execution of the annotated method.
 * </p>
 *
 * <p>
 * The expected HTTP header for authentication is {@code X-Commerzi-Auth}. The aspect uses this session value to retrieve
 * user information via the {@link IAuthentificationService}.
 * </p>
 *
 * @see CommerziAuthenticated
 * @see IAuthentificationService
 * @see CommerziUser
 */
@Aspect
@Component
public class CommerziAuthenticatedAspect {

    /** Authentication service */
    @Autowired
    private IAuthentificationService authentificationService;

    /**
     * Method that intercepts calls to methods annotated with {@link CommerziAuthenticated}.
     * <p>
     * This method checks if the user is authenticated by consulting the {@code X-Commerzi-Auth} header.
     * If the user is not found, an HTTP 401 (Unauthorized) response is returned.
     * If the user is authenticated, the method proceeds with its normal execution.
     * </p>
     *
     * @param joinPoint The execution point of the intercepted method.
     * @return The return value of the method, or an HTTP 401 response if the user is not authenticated.
     * @throws Throwable If an exception is thrown during the method execution.
     */
    @Around("@annotation(com.commerzi.commerziapi.security.CommerziAuthenticated)")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        String session = Security.getSessionFromSpring();

        if (session == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        CommerziUser commerziUser = authentificationService.getUserBySession(session);
        if (commerziUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return joinPoint.proceed();
    }

} // plunger