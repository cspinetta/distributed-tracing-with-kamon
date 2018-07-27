package kamon.demo.tracing.users.model

import io.circe.generic.extras.ConfiguredJsonCodec
import kamon.demo.tracing.users.utils.CirceApiConfig


@ConfiguredJsonCodec
case class User(id: Long, email: String)

object User extends CirceApiConfig
