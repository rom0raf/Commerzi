package com.commerzi.commerziapi.security;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.service.IAuthentificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test pour CommerziAuthentificatedAspect
 */
class CommerziAuthenticatedAspectTest {

    @Mock
    private IAuthentificationService authentificationService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private CommerziAuthenticatedAspect commerziAuthenticatedAspect;

    private static final String VALID_SESSION = "valid-session";
    private static final String INVALID_SESSION = "invalid-session";

    private CommerziUser commerziUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commerziUser = new CommerziUser();
    }

    @Test
    void testCheckAuthentication_authenticatedUser() throws Throwable {
        // Arrange
        // Créez une requête simulée avec un header "AUTHENTIFICATION_HEADER_NAME" valide
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(Security.AUTHENTIFICATION_HEADER_NAME, VALID_SESSION);

        // Enrobez la requête dans un ServletRequestAttributes et définissez-la dans RequestContextHolder
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Comportement attendu du service d'authentification
        when(authentificationService.getUserBySession(VALID_SESSION)).thenReturn(commerziUser);
        when(joinPoint.proceed()).thenReturn("success");

        // Act
        Object result = commerziAuthenticatedAspect.checkAuthentication(joinPoint);

        // Assert
        assertEquals("success", result);
        verify(authentificationService).getUserBySession(VALID_SESSION);
        verify(joinPoint).proceed();
    }

    @Test
    void testCheckAuthentication_unauthenticatedUser() throws Throwable {
        // Arrange
        // Créez une requête simulée avec un header "AUTHENTIFICATION_HEADER_NAME" invalide
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(Security.AUTHENTIFICATION_HEADER_NAME, INVALID_SESSION);

        // Enrobez la requête dans un ServletRequestAttributes et définissez-la dans RequestContextHolder
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Comportement attendu du service d'authentification
        when(authentificationService.getUserBySession(INVALID_SESSION)).thenReturn(null);

        // Act
        Object result = commerziAuthenticatedAspect.checkAuthentication(joinPoint);

        // Assert
        assertEquals(ResponseEntity.status(401).body("Unauthorized"), result);
        verify(authentificationService).getUserBySession(INVALID_SESSION);
        verify(joinPoint, never()).proceed();  // La méthode joinPoint.proceed() ne doit pas être appelée.
    }
}