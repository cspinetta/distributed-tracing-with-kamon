package kamon.demo.tracing.front.model

import java.time.{LocalDate, LocalDateTime}

case class Item(id: Long, title: String, category: String, sellerId: Long)

case class ItemDetails(item: Item, seller: Option[Seller], user: User)
case class Seller(id: Long, firstName: String, lastName: String,
                  birthday: LocalDate, category: String,
                  registrationDate: LocalDateTime, grade: BigDecimal)
case class User(id: Long, email: String)
