# On part d'une image avec un JDK 17
FROM amazoncorretto:17

# On copie le jar de l'application
COPY ./commerzi-api-0.0.1-SNAPSHOT.jar /app/commerzi-api-0.0.1-SNAPSHOT.jar

# On lance l'application en redefinissant ENTRYPOINT
ENTRYPOINT ["java", "-jar", "/app/commerzi-api-0.0.1-SNAPSHOT.jar"]