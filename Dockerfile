FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /build

COPY caronas/pom.xml .

RUN mvn dependency:go-offline

COPY caronas/src ./src

RUN mvn package -DskipTests


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /build/target/caronas-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "caronas-0.0.1-SNAPSHOT.jar"]