package kamon.demo.tracing.search.api

import cats.effect.IO
import kamon.demo.tracing.search.utils.LogSupport
import org.http4s.HttpService
import kamon.demo.tracing.search.BuildInfo
import org.http4s.dsl.io._

class HealthService extends LogSupport {

  def service(): HttpService[IO] = {
    def route = HttpService[IO] {
      case GET -> Root => Ok(fullVersion)
    }
    route
  }

  private def fullVersion: String = {
    val version = s"${BuildInfo.name}:${BuildInfo.version}"
    log.info(s"Demo health-check: $version")
    version
  }
}

object HealthService {
  def apply(): HealthService = new HealthService()
}
