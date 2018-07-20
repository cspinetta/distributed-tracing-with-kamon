package kamon.demo.tracing.user.api

import cats.effect.IO
import kamon.demo.tracing.user.model.User
import kamon.demo.tracing.user.utils.LogSupport
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl.io._

case object UserService extends LogSupport {
  import kamon.demo.tracing.user.utils.CirceUtils.circeCustomSyntax._

  def service: HttpService[IO] = {

    def route: HttpService[IO] = HttpService[IO] {
      case GET -> Root / LongVar(userId) => handleGetUser(userId)
    }

    def handleGetUser(userId: Long): IO[Response[IO]] = {
      if (userId < 100) Ok(User(id = userId, email = s"user-$userId@domain.com").asJson)
      else              NotFound(())
    }

    route
  }
}
