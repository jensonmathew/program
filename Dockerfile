FROM openjdk:8u111-jdk-alpine
ADD /target/*.jar app.jar
EXPOSE 19000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
