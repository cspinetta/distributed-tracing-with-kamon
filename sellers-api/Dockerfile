FROM openjdk:8-jre-alpine

COPY ./build/distributions/sellers-api-0.0.1-SNAPSHOT/ /project

WORKDIR /project

EXPOSE 9072

CMD export "SELLERS_API_OPTS=-javaagent:/project/kanela-agent-bundle-0.0.14.jar -Dkamon.zipkin.host=zipkin -Dkamon.zipkin.port=9411 -Dkamon.jaeger.host=jaeger"; /project/bin/sellers-api
