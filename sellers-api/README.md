Sellers API
======

### Dependencies
- [Java] > v1.8.x
- [Kotlin] v1.2.x
- [SparkJava] v2.7.x
- [H2 DB] v1.4.x
- [Kamon] v1.x
- [Kanela] v0.x

For `build` and `run`: [Gradle].

### Run

```bash
./gradlew -i run
```

The Sellers-API will start up on the HTTP port at <http://localhost:9072/>.
You can change the settings by `application.conf` file.

### Endpoints

```bash
http 'http://localhost:9072/api/sellers/{id}'
```

[Gradle]:https://gradle.org/
[Java]:https://www.java.com/es/download/
[Kotlin]:https://kotlinlang.org/
[SparkJava]:http://sparkjava.com/
[H2 DB]:http://www.h2database.com
[Kamon]:http://kamon.io
[Kanela]:http://kamon-io.github.io/kanela/
