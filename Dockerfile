FROM eclipse-temurin:21.0.2_13-jre-jammy
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]