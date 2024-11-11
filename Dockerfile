# 최종 이미지 단계
FROM openjdk:17-jdk-alpine as final

WORKDIR /app

# 빌드된 JAR 파일과 정적 문서 파일을 복사
COPY ./build/libs/tickit-0.0.1-SNAPSHOT.jar app.jar
COPY ./build/resources/main/static/docs /app/static/docs

# HEALTHCHECK 추가
HEALTHCHECK --interval=5s --timeout=3s --start-period=30s --retries=3 \
  CMD curl --fail http://localhost:8090 || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8090