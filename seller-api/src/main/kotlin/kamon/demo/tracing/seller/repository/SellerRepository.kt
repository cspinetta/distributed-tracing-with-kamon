package kamon.demo.tracing.seller.repository

import kamon.demo.tracing.seller.model.Seller
import kamon.demo.tracing.seller.model.SellerCategory
import kotliquery.*

class SellerRepository {

    fun findById(id: Long): Seller? {
        return using(sessionOf(HikariCP.dataSource())) { session ->
            session.run(queryOf("select id, first_name, last_name, birthday, category, " +
                    "registration_date, grade, active from seller where id=?", id).map(toSeller).asSingle)
            }
    }

    private val toSeller: (Row) -> Seller = { row -> Seller(row.long(1), row.string(2),
            row.string(3), row.localDate(4), SellerCategory.valueOf(row.string(5)),
            row.localDateTime(6), row.bigDecimal(7), row.boolean(8))
    }
}
