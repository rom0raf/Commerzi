package com.commerzi.commerziapi.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * L'annotation {@code CommerziAuthenticated} est utilisée pour marquer une méthode comme nécessitant un utilisateur authentifié.
 * <p>
 * Cette annotation est utilisé dans un contexte spring, sur des mapping
 * </p>
 * 
 * <p>
 * Exemple d'utilisation :
 * </p>
 * 
 * <pre>
 * {@code
 * @GetMapping("/") // Ou tout autre mapping spring
 * @CommerziAuthenticated
 * public void maMethodeSecuriisee() {
 *     // Méthode nécessitant une authentification
 * }
 * }
 * </pre>
 * 
 * @see java.lang.annotation.ElementType#METHOD
 * @see java.lang.annotation.RetentionPolicy#RUNTIME
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommerziAuthenticated {}