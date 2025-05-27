# 🛠️ Build stage using Azul Zulu JDK 21
FROM azul/zulu-openjdk:21.0.7-jdk AS build

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

RUN chmod +x gradlew

ENV IN_DOCKER=true

RUN ./gradlew clean build -x test --no-daemon

# 🏃 Runtime stage using Azul Zulu JRE 21
FROM azul/zulu-openjdk:21.0.7-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-Denvironment=staging", "-jar", "app.jar", "--server.address=0.0.0.0"]
