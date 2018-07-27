package kamon.demo.tracing.users.conf

import scala.concurrent.duration.FiniteDuration

trait ConfigSupport {
  protected lazy val config: Config.AppConfig = Config.loadConfig
}

object Config extends ConfigLoader {

  def loadConfig: Config.AppConfig = pureconfig.loadConfigOrThrow[Config.AppConfig](config)

  case class AppConfig(server: ServerConfig, client: ClientConfig)

  case class ClientConfig(maxTotalConnections: Int, idleTimeout: FiniteDuration, requestTimeout: FiniteDuration)
  case class ServerConfig(host: String, port: Int)

  lazy val appConfig: AppConfig = pureconfig.loadConfigOrThrow[AppConfig](config)
}
