package kamon.demo.tracing.users.conf

import java.io.File

import com.typesafe.config.{ConfigFactory, Config => TypeSafeConfig}
import kamon.demo.tracing.users.utils.LogSupport

trait ConfigLoader {
  val config: TypeSafeConfig = ConfigLoader.configuration
  val currentEnvironment: String = Option(System.getProperty(ConfigLoader.EnvironmentKey)).getOrElse(config.getString(ConfigLoader.EnvironmentKey))
}

object ConfigLoader extends LogSupport {
  val EnvironmentKey = "environment"

  val configuration: TypeSafeConfig = {
    val defaultConfig = ConfigFactory.load()
    val overrideFile = new File(Option(System.getProperty("environmentOverride")).getOrElse("environment-override.conf"))
    val environment = Option(System.getProperty(EnvironmentKey)).getOrElse(defaultConfig.getString(EnvironmentKey))

    log.info(s"Loading config from [$environment] overriding with [$overrideFile]")

    ConfigFactory.parseFile(overrideFile)
      .withFallback(defaultConfig.getConfig(environment)).withFallback(defaultConfig)
  }

}