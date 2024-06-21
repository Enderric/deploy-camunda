# Используем официальный образ Maven для сборки артефактов
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY settings.xml /home/app/settings.xml
VOLUME ["/root/.m2"]
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -s /home/app/settings.xml -f /home/app/pom.xml clean package

# Используем официальный образ OpenJDK для запуска приложения
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /home/app/target/camunda-1.0.5.jar /usr/local/lib/camunda-1.0.5.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/camunda-1.0.5.jar"]
