package kamon.demo.tracing.users.utils

import io.circe.Printer
import io.circe.generic.extras.Configuration
import kamon.demo.tracing.users.utils.CirceUtils.circeApiConfig
import org.http4s.circe.CirceInstances

object CirceUtils {
  val circeCustomSyntax: CirceInstances = CirceInstances.withPrinter(Printer.spaces2.copy(dropNullValues = true))

  val circeApiConfig: Configuration = Configuration.default.withSnakeCaseMemberNames.withSnakeCaseConstructorNames.withDefaults
}

trait CirceApiConfig {
  implicit val customConfig: Configuration = circeApiConfig
}
