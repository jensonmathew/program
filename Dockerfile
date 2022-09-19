## Below command will download image from Docker Hub
FROM amazoncorretto:11-alpine

## Add TMO self-signed certs for avoid PKIX exceptions
COPY tmo-*.crt /usr/local/share/ca-certificates/
RUN for i in $(find /usr/local/share/ca-certificates -name "*.crt"); do echo "$i"; keytool -importcert -alias $(mktemp tmp.XXXXXXXX) -cacerts -file $i -storepass 'changeit' -noprompt; rm -rf tmp.*; done;

## [Optional] Test Java SSL connection to TMO signed certs
#RUN wget https://raw.githubusercontent.com/MichalHecko/SSLPoke/master/src/main/java/sk/mhecko/ssl/SSLPoke.java
#RUN java SSLPoke.java www.px-npe1103.pks.t-mobile.com 443

## Variable intake from .gitlab-ci.yml 
ARG APP_VERSION
ARG PROJECT_NAME
ARG COMMIT_SHA

## Commands to be executed during the image build
COPY target/$PROJECT_NAME-$APP_VERSION-$COMMIT_SHA.jar /tmo/

RUN ln -s /tmo/$PROJECT_NAME-$APP_VERSION-$COMMIT_SHA.jar /tmo/helloworld.jar

ENTRYPOINT ["java", "-jar", "/tmo/helloworld.jar"]
