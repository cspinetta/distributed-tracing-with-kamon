FROM openjdk:8-jdk-alpine

COPY ./target/universal/stage /project

RUN apk update; apk upgrade; apk add bash

WORKDIR /project

EXPOSE 9070

CMD ["/project/bin/front-api", "-J-javaagent:/project/kanela-agent-bundle-0.0.14.jar", "-Dpidfile.path=/dev/null"]
