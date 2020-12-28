FROM openjdk:11
EXPOSE 9003
ARG JAR_FILE=build/libs/rewards-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]