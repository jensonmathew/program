## Below command will download image from Docker Hub
FROM registry.gitlab.com/tmobile/citadel/containers/tmo-java-base:17

## Variable intake from .gitlab-ci.yml 
ARG APP_VERSION
ARG CI_PROJECT_NAME
ARG CI_COMMIT_SHORT_SHA

## Commands to be executed during the image build
COPY target/$CI_PROJECT_NAME-$APP_VERSION-$CI_COMMIT_SHORT_SHA.jar /run/

RUN ln -s /run/$CI_PROJECT_NAME-$APP_VERSION-$CI_COMMIT_SHORT_SHA.jar /run/app.jar

ENTRYPOINT exec java $JAVA_OPTS -jar /run/app.jar
