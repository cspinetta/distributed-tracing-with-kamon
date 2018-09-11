FROM openjdk:8-jre-alpine

COPY ./build/distributions/items-api-0.0.1-SNAPSHOT/ /project

WORKDIR /project

EXPOSE 9071

CMD export "ITEMS_API_OPTS=-javaagent:/project/kanela-agent-bundle-0.0.14.jar -Dservice.sellers-api.host=http://sellers-api:9072 -Dkamon.zipkin.host=zipkin -Dkamon.zipkin.port=9411 -Dkamon.jaeger.host=jaeger"; /project/bin/items-api
