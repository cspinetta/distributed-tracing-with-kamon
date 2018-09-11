FROM openjdk:8-jre-alpine

COPY ./target/scala-2.12 /project

WORKDIR /project


EXPOSE 9073

CMD ["java", "-Dkamon.zipkin.host=zipkin", "-Dkamon.zipkin.port=9411", "-Dkamon.jaeger.host=jaeger", "-jar", "/project/users-api_2.12-0.0.1-SNAPSHOT-one-jar.jar"]
