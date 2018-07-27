Items API
======

### Dependencies
- [Java] > v1.8.x
- [SpringBoot] v2.x.x
- [Kamon] v1.x
- [Kanela] v0.x

For `build` and `run`: [Gradle].

### Run

```bash
./gradlew -i run
```

The Items-API will start up on the HTTP port at <http://localhost:9071/>.
You can change the settings by `application.conf` file.

### Endpoints

```bash
http 'http://localhost:9071/api/items/search?key-word={some-string}'
http 'http://localhost:9071/api/items/{id}'
http 'http://localhost:9071/api/items/{id}/details'
```

[Gradle]:https://gradle.org/
[Java]:https://www.java.com/es/download/
[SpringBoot]:https://spring.io/projects/spring-boot
[Kamon]:http://kamon.io
[Kanela]:http://kamon-io.github.io/kanela/
