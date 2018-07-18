package kamon.demo.tracing.search.model

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import kamon.demo.tracing.search.utils.CirceUtils.circeApiConfig

case class Item(id: Long, title: String, category: String, sellerId: Long)

object Item {
  implicit val customConfig: Configuration = circeApiConfig

  implicit val encoder: Encoder[Item] = deriveEncoder
  implicit val decoder: Decoder[Item] = deriveDecoder
}
