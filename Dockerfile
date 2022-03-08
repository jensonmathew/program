## Below command will download image from Docker Hub
FROM azul/zulu-openjdk-alpine:8

## Variable intake from .gitlab-ci.yml 
ARG APP_VERSION
ARG PROJECT_NAME
ARG COMMIT_SHA

## Commands to be executed during the image build
COPY target/$PROJECT_NAME-$APP_VERSION-$COMMIT_SHA.jar /tmo/

RUN ln -s /tmo/$PROJECT_NAME-$APP_VERSION-$COMMIT_SHA.jar /tmo/helloworld.jar

ENTRYPOINT ["java", "-jar", "/tmo/helloworld.jar"]
