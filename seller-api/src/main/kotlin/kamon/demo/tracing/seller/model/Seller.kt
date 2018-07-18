package kamon.demo.tracing.seller.model


import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class Seller(val id: Long, val firstName: String, val lastName: String,
                  val birthday: LocalDate, val category: SellerCategory,
                  val registrationDate: LocalDateTime, val grade: BigDecimal,
                  val active: Boolean)

enum class SellerCategory {
    BRONZE, SILVER, GOLD, PLATINUM
}