package kamon.demo.tracing.user.api

import cats.effect.IO
import kamon.Kamon
import kamon.demo.tracing.user.utils.LogSupport
import kamon.executors.util.ContextAwareExecutorService
import kamon.trace.Span
import org.http4s._
import org.http4s.dsl.io._

import scala.util.Random

case class StatsService(ec: ContextAwareExecutorService) extends LogSupport {
  import StatsService._

  val r: Random = scala.util.Random

  def service: HttpService[IO] = {

    def route: HttpService[IO] = HttpService[IO] {
      case GET -> Root / LongVar(userId) => handleStatsUser(userId)
    }

    def handleStatsUser(userId: Long): IO[Response[IO]] = {
      IO.pure(executeStats(userId))
        .flatMap(_ => Ok(()))
    }

    route
  }

  private def executeStats(userId: Long): Unit = {
    ec.execute(() => {
      val statsExecutionSpan = Kamon.buildSpan("stats.execution")
        .asChildOf(Kamon.currentContext().get(Span.ContextKey))
        .withMetricTag("span.kind", "cpu-intensive-operation")
        .withTag("user.id", userId)
        .start()
      Kamon.withContext(Kamon.currentContext().withKey(Span.ContextKey, statsExecutionSpan)) {
        Thread.sleep(r.nextInt(MaxTimeExecuting.toMillis.toInt))
        Kamon.currentSpan().finish()
      }
    })
  }
}

object StatsService {
  import scala.concurrent.duration._

  val MaxTimeExecuting: FiniteDuration = 10.seconds
}
