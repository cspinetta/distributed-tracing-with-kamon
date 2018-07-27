Users API
======

### Dependencies
- [Java] > v1.8.x
- [Scala] v2.12.x
- [Http4s] v0.18.x
- [Circe] v0.8.x
- [Kamon] v1.x

For `build` and `run`: [SBT].

### Run

```bash
sbt run
```

The Users-API will start up on the HTTP port at <http://localhost:9073/>.
You can change the settings by `application.conf` file.

### Endpoints

```bash
http 'http://localhost:9073/api/users/{user-id}'
```

Some considerations:
* user id > 100 => 404
* user id > 50  => return users and execute stats asynchronously
* else          => just return an user

[SBT]:https://www.scala-sbt.org/
[Java]:https://www.java.com/es/download/
[Scala]:https://www.scala-lang.org/
[Http4s]:http://http4s.org/
[Circe]:https://circe.github.io/circe/
[Kamon]:http://kamon.io