package kamon.demo.tracing.user.client

import cats.effect.IO
import kamon.demo.tracing.user.conf.ConfigSupport
import kamon.demo.tracing.user.utils._
import org.http4s.client.Client
import org.http4s.{Method, Request, Uri}

class StatsClient(client: Client[IO]) extends ConfigSupport {
  import UriUtils._

  private val c = config.service.statsApi
  private lazy val uriPrototype = Uri.fromString(s"${c.host}${c.endpoint}")

  def informUser(userId: Long): IO[Unit] = {
    val uri = uriPrototype.map(uri => uri / "user" / s"$userId").parse
    client.fetch(Request[IO](Method.GET, uri))(_ => IO.pure(()))
  }
}
