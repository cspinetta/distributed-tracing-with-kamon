package kamon.demo.tracing.user

import java.util.concurrent.{ExecutorService, Executors}

import cats.effect._
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kamon.Kamon
import kamon.demo.tracing.user.api.{HealthService, StatsService, UserService}
import kamon.demo.tracing.user.client.StatsClient
import kamon.demo.tracing.user.conf.{ConfigLoader, ConfigSupport}
import kamon.demo.tracing.user.program.UserProgram
import kamon.demo.tracing.user.utils.ThreadUtils._
import kamon.executors.util.ContextAwareExecutorService
import kamon.http4s.middleware.client.{KamonSupport => KamonSupportC}
import kamon.http4s.middleware.server.{KamonSupport => KamonSupportS}
import org.http4s._
import org.http4s.client.Client
import org.http4s.client.blaze.{BlazeClientConfig, Http1Client}
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext

object Server extends StreamApp[IO] with ConfigSupport with Programs with ClientFactory {

  private val executor: ExecutorService = ContextAwareExecutorService(Executors.newFixedThreadPool(30, namedThreadFactory("search-server-pool")))
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(executor)

  private val statsExecutor = ContextAwareExecutorService(Executors.newFixedThreadPool(8, namedThreadFactory("stats-execution-pool")))

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {

    def router(client: Client[IO]): HttpService[IO] = KamonSupportS {
      Logger(logHeaders = true, logBody = false) {
        Router[IO](mappings =
          "/api/user/health-check"  -> HealthService().service(),
          "/api/user"               -> UserService(userProgram(client)).service,
          "/api/stats/user"         -> StatsService(statsExecutor).service
        )
      }
    }

    for {
      _ <-  Stream.eval(Sync[IO].delay(initKamon()))
      client   <- httpClient
      exitCode <- BlazeBuilder[IO]
        .bindHttp(config.server.port, config.server.host)
        .mountService(router(client))
        .serve
    } yield exitCode
  }

  def initKamon(): Unit = {
    Kamon.reconfigure(ConfigLoader.configuration)
    Kamon.loadReportersFromConfig()
  }
}

trait Clients {
  def statsClient(client: Client[IO]): StatsClient = new StatsClient(client)
}

trait Programs extends Clients {
  def userProgram(client: Client[IO]) = new UserProgram(statsClient(client))
}

trait ClientFactory extends ConfigSupport {
  private lazy val clientConfig = BlazeClientConfig.defaultConfig.copy(
    maxTotalConnections = config.client.maxTotalConnections,
    idleTimeout = config.client.idleTimeout,
    requestTimeout = config.client.requestTimeout
  )
  def httpClient: Stream[IO, Client[IO]] = Http1Client
    .stream[IO](clientConfig)
    .map(client => KamonSupportC(client))
}