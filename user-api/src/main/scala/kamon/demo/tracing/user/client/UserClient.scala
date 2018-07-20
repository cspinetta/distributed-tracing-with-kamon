package kamon.demo.tracing.user.client

import cats.effect.IO
import kamon.demo.tracing.user.conf.ConfigSupport
import kamon.demo.tracing.user.model.User
import kamon.demo.tracing.user.utils._
import org.http4s.Uri
import org.http4s.client.Client

class UserClient(client: Client[IO]) extends ConfigSupport {
  import CirceUtils.circeCustomSyntax._
  import UriUtils._

  private val c = config.service.userApi
  private lazy val uriPrototype = Uri.fromString(s"${c.host}${c.endpoint}")

  def findById(userId: Long): IO[User] = {
    val uri = uriPrototype.map(uri => uri / s"$userId").parse
    client.expect(uri)(jsonOf[IO, User])
  }
}
