package com.commerzi.commerziapi.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code CommerziAuthenticated} annotation is used to mark a method as requiring an authenticated user.
 * <p>
 * This annotation is used in a Spring context, on mapping methods.
 * </p>
 *
 * <p>
 * Example of usage:
 * </p>
 *
 * <pre>
 * {@code
 * @GetMapping("/") // Or any other Spring mapping
 * @CommerziAuthenticated
 * public void mySecureMethod() {
 *     // Method that requires authentication
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