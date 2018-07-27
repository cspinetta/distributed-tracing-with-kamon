package kamon.demo.tracing.sellers

import com.typesafe.config.ConfigFactory
import kamon.Kamon
import kamon.demo.tracing.sellers.base.DBSetup.setupDB
import kamon.demo.tracing.sellers.repository.SellersRepository
import spark.Spark.ipAddress
import spark.Spark.port

fun main(args: Array<String>) {
    Kamon.loadReportersFromConfig()
    port(config.getInt("server.port"))
    ipAddress(config.getString("server.host"))
    setupDB()
    Routes.init(sellerRepository)
}

val sellerRepository = SellersRepository()
val config = ConfigFactory.load().resolve()!!
