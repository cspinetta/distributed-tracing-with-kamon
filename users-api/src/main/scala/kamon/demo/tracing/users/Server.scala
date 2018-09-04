package kamon.demo.tracing.users

import java.util.concurrent.{ExecutorService, Executors}

import cats.effect._
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kamon.Kamon
import kamon.demo.tracing.users.api.{HealthService, UsersService}
import kamon.demo.tracing.users.conf.{ConfigLoader, ConfigSupport}
import kamon.demo.tracing.users.program.{StatsProgram, UsersProgram}
import kamon.demo.tracing.users.utils.ThreadUtils._
import kamon.http4s.middleware.client.{KamonSupport => KamonSupportC}
import kamon.http4s.middleware.server.{KamonSupport => KamonSupportS}
import org.http4s._
import org.http4s.client.Client
import org.http4s.client.blaze.{BlazeClientConfig, Http1Client}
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext

object Server extends StreamApp[IO] with ConfigSupport with Programs with ClientsFactory {

  private val executor: ExecutorService = Executors.newFixedThreadPool(30, namedThreadFactory("user-api-server-pool"))
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(executor)

  private val statsExecutor: ExecutorService = Executors.newFixedThreadPool(8, namedThreadFactory("stats-execution-pool"))

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {

    def router(client: Client[IO]): HttpService[IO] = KamonSupportS {
      Logger(logHeaders = true, logBody = false) {
        Router[IO](mappings =
          "/api/users/health-check"  -> HealthService().service(),
          "/api/users"               -> UsersService(usersProgram(statsProgram(statsExecutor))).service
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
  // TODO: items api client and call on stats processing
//  def statsClient(client: Client[IO]): StatsClient = new StatsClient(client)
}

trait Programs extends Clients {
  def usersProgram(statsProgram: StatsProgram) = new UsersProgram(statsProgram)
  def statsProgram(executor: ExecutorService) = new StatsProgram(executor)
}

trait ClientsFactory extends ConfigSupport {
  private lazy val clientConfig = BlazeClientConfig.defaultConfig.copy(
    maxTotalConnections = config.client.maxTotalConnections,
    idleTimeout = config.client.idleTimeout,
    requestTimeout = config.client.requestTimeout
  )
  def httpClient: Stream[IO, Client[IO]] = Http1Client
    .stream[IO](clientConfig)
    .map(client => KamonSupportC(client))
}
