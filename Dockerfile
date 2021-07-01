## Below command will download image from Docker Hub
FROM alpine:latest

## Variable intake from .gitlab-ci.yml 
ARG APP_VERSION
ARG PROJECT_NAME
ARG COMMIT_SHA

## Install troubleshooting packages
RUN apk update
RUN apk add --no-cache openjdk8=8.292.10-r0 

## Commands to be executed during the image build
COPY target/$PROJECT_NAME-$APP_VERSION-$COMMIT_SHA.jar /home/
RUN ls /home
COPY entrypoint.sh /usr/local/entrypoint.sh
RUN chmod +x /usr/local/entrypoint.sh
ENTRYPOINT ["/usr/local/entrypoint.sh"]
