package com.commerzi.commerziapi.security;

import com.commerzi.commerziapi.model.CommerziUser;
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

/**
 * Aspect qui gère la vérification de l'authentification pour les méthodes annotées avec {@link CommerziAuthenticated}.
 * <p>
 * Cette classe est responsable de vérifier si l'utilisateur est authentifié avant d'exécuter les méthodes annotées par {@code @CommerziAuthenticated}.
 * Si l'utilisateur n'est pas authentifié, elle retourne une réponse HTTP 401 (Unauthorized). Si l'utilisateur est authentifié,
 * elle permet l'exécution normale de la méthode annotée.
 * </p>
 *
 * <p>
 * Le header HTTP attendu pour l'authentification est {@code X-Commerzi-Auth}. L'aspect utilise cette valeur de session pour récupérer
 * les informations sur l'utilisateur via le service {@link IAuthentificationService}.
 * </p>
 *
 * @see CommerziAuthenticated
 * @see IAuthentificationService
 * @see CommerziUser
 */
@Aspect
@Component
public class CommerziAuthenticatedAspect {

    /** Service d'authentification */
    @Autowired
    private IAuthentificationService authentificationService;

    /**
     * Méthode qui intercepte les appels des méthodes annotées avec {@link CommerziAuthenticated}.
     * <p>
     * Cette méthode vérifie si l'utilisateur est authentifié en consultant le header {@code X-Commerzi-Auth}.
     * Si l'utilisateur n'est pas trouvé, une réponse HTTP 401 (Unauthorized) est renvoyée.
     * Si l'utilisateur est authentifié, la méthode continue son exécution normalement.
     * </p>
     *
     * @param joinPoint Le point d'exécution de la méthode interceptée.
     * @return La valeur de retour de la méthode, ou une réponse HTTP 401 si l'utilisateur n'est pas authentifié.
     * @throws Throwable Si une exception est l evée pendant l'exécution de la méthode.
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

}