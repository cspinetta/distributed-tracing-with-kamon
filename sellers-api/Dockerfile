FROM openjdk:8-jre-alpine

COPY ./build/distributions/sellers-api-0.0.1-SNAPSHOT/ /project

WORKDIR /project

EXPOSE 9072

CMD export "SELLERS_API_OPTS=-javaagent:/project/kanela-agent-bundle-0.0.14.jar"; /project/bin/sellers-api