Users API
======

### Dependencies
- [Java] > v1.8.x
- [Scala] v2.12.x
- [Play] v2.6.x
- [Kamon] v1.x
- [Kanela] v0.x

For `build` and `run`: [SBT].

### Run

```bash
sbt stage
./target/universal/stage/bin/front-api -J-javaagent:/path/to/kanela-agent-bundle.jar
```

The Front-API will start up on the HTTP port at <http://localhost:9070/>.
You can change the settings by `application.conf` file.

### Endpoints

```bash
http 'http://localhost:9070/api/front/search?key-word={some-string}'
http 'http://localhost:9070/api/front/details/item/{item-id}/user/{user-id}'
```

```bash
http 'http://localhost:9070/api/front/parallel/search?key-word={some-string}'
http 'http://localhost:9070/api/front/parallel/details/item/{item-id}/user/{user-id}'
```

[SBT]: https://www.scala-sbt.org/
[Java]: https://www.java.com/es/download/
[Scala]: https://www.scala-lang.org/
[Play]: https://www.playframework.com/
[Kamon]: http://kamon.io
[Kanela]: http://kamon-io.github.io/kanela/
