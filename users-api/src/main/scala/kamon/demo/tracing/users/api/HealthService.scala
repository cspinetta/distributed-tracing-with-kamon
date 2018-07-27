package kamon.demo.tracing.users.api

import cats.effect.IO
import kamon.demo.tracing.users.utils.LogSupport
import org.http4s.HttpService
import org.http4s.dsl.io._

class HealthService extends LogSupport {

  def service(): HttpService[IO] = {
    def route = HttpService[IO] {
      case GET -> Root => Ok(fullVersion)
    }
    route
  }

  private def fullVersion: String = "Health-check ok"
}

object HealthService {
  def apply(): HealthService = new HealthService()
}
