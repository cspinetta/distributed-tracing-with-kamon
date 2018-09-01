Demo for tech talk [Distributed Tracing][1]
======

## Stack:
* [Zipkin][2]
* [Jaeger][3]
* [Kamon 1.x][4]
* [Kanela][5]
* [JDK 1.8 (or greater)][6]

## Getting started

The demo is composed by 4 API's: `items-api`, `users-api`, `sellers-api` and `front-api`.
You'll find each one in its own sub folder with a readme and some example requests.
The simplest way to run this demo is enter on each api and start it up as its readme indicate.
Also you have to start a `zipkin` and/or a `jaeger` server for saving and inspecting the generated traces.
Then just send some requests to the `front-api` and go to the tracing server to inspect your traces.

_A docker compose with all APIs and traces servers integrated **is coming!**_

### Interesting use cases

#### Basic example
```bash
http 'http://localhost:9070/api/front/search?key-word=robot'
```

#### Compose 2 services
```bash
http 'http://localhost:9070/api/front/details/item/15/user/5'
```

#### The same but in parallel
```bash
http 'http://localhost:9070/api/front/parallel/details/item/15/user/5'
```

#### Async span
```bash
http 'http://localhost:9070/api/front/parallel/details/item/15/user/55'
```

#### Fail with 500. Item not found
```bash
http 'http://localhost:9070/api/front/parallel/details/item/1500/user/55'
```

[1]: https://slides.com/diegoparra/monitoring-microservices
[2]: https://zipkin.io/
[3]: https://www.jaegertracing.io/
[4]: https://kamon.io
[5]: http://kamon-io.github.io/kanela/
[6]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
