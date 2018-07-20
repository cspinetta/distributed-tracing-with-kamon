package kamon.demo.tracing.user.model

import io.circe.generic.extras.ConfiguredJsonCodec
import kamon.demo.tracing.user.utils.CirceApiConfig


@ConfiguredJsonCodec
case class User(id: Long, email: String)

object User extends CirceApiConfig
