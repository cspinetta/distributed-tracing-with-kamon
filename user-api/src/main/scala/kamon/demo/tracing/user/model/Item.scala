package kamon.demo.tracing.user.model

import java.time.{LocalDate, LocalDateTime}

import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}
import io.circe.java8.time.TimeInstances
import kamon.demo.tracing.user.utils.CirceApiConfig
import kamon.demo.tracing.user.utils.CirceUtils.circeApiConfig


@ConfiguredJsonCodec
case class Item(id: Long, title: String, category: String, sellerId: Long)

object Item {
  implicit val customConfig: Configuration = circeApiConfig
}

@ConfiguredJsonCodec
case class ItemDetails(item: Item, seller: Seller, user: User)

@ConfiguredJsonCodec
case class Seller(id: Long, firstName: String, lastName: String,
                  birthday: LocalDate, category: String,
                  registrationDate: LocalDateTime, grade: BigDecimal)

@ConfiguredJsonCodec case class User(id: Long, email: String)

object ItemDetails extends CirceApiConfig
object Seller extends CirceApiConfig with TimeInstances
object User extends CirceApiConfig
