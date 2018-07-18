package kamon.demo.tracing.search.utils

import io.circe.Printer
import io.circe.generic.extras.Configuration
import org.http4s.circe.CirceInstances

object CirceUtils {
  val circeCustomSyntax: CirceInstances = CirceInstances.withPrinter(Printer.spaces2.copy(dropNullValues = true))

  val circeApiConfig: Configuration = Configuration.default.withSnakeCaseMemberNames.withSnakeCaseConstructorNames.withDefaults
}
