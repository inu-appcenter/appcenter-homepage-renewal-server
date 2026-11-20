# Build stage
FROM gradle:7.6-jdk11 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
RUN gradle bootJar -x test --no-daemon

# Run stage
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# 힙 상한. 서버에서 JAVA_OPTS 환경변수로 덮어쓸 수 있다.
# (예전 형식 ["java","-jar","app.jar"]은 셸을 거치지 않아 $JAVA_OPTS가 적용되지 않았다)
ENV JAVA_OPTS="-Xms256m -Xmx768m -XX:+ExitOnOutOfMemoryError"
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
