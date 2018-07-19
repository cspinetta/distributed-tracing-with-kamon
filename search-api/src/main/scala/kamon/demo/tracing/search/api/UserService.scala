package kamon.demo.tracing.search.api

import cats.effect.IO
import kamon.demo.tracing.search.model.User
import kamon.demo.tracing.search.utils.LogSupport
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl.io._

case object UserService extends LogSupport {
  import kamon.demo.tracing.search.utils.CirceUtils.circeCustomSyntax._

  def service: HttpService[IO] = {

    def route: HttpService[IO] = HttpService[IO] {
      case GET -> Root / LongVar(userId) => handleGetUser(userId)
    }

    def handleGetUser(userId: Long): IO[Response[IO]] =
      Ok(User(id = userId, email = s"user-$userId@domain.com").asJson)

    route
  }
}
