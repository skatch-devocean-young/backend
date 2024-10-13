FROM openjdk:17-jdk-slim as build

WORKDIR /app

COPY ./build/libs/onetime-0.0.1-SNAPSHOT.jar app.jar

FROM openjdk:17-jdk-alpine as final

WORKDIR /app

COPY --from=build /app/app.jar app.jar

HEALTHCHECK --interval=5s --timeout=3s --start-period=30s --retries=3 \
  CMD curl --fail http://localhost:8090 || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]