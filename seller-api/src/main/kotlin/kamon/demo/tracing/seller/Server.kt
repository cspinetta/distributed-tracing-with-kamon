package kamon.demo.tracing.seller

import com.typesafe.config.ConfigFactory
import kamon.Kamon
import kamon.demo.tracing.seller.base.DBSetup.setupDB
import kamon.demo.tracing.seller.repository.SellerRepository
import spark.Spark.ipAddress
import spark.Spark.port

//import spark.kotlin.ipAddress
//import spark.kotlin.port

fun main(args: Array<String>) {
    Kamon.loadReportersFromConfig()
    port(config.getInt("server.port"))
    ipAddress(config.getString("server.host"))
    setupDB()
    Routes.init(sellerRepository)
}

val sellerRepository = SellerRepository()
val config = ConfigFactory.load().resolve()!!
