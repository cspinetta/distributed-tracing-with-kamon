package kamon.demo.tracing.sellers

import kamon.demo.tracing.sellers.base.JsonSupport
import kamon.demo.tracing.sellers.repository.SellersRepository
import mu.KLogging
import spark.Request
import spark.Response
import spark.ResponseTransformer
import spark.Route
import spark.Spark.*

object Routes : KLogging() {

    fun init(sellersRepository: SellersRepository) {

        before("/*") { req, _ -> logger.info("Request: ${req.pathInfo()}") }

        get("/api/sellers/:id", Route { req: Request, res: Response ->
            val id: Long = req.params(":id").toLong()
            sellersRepository.findById(id)?.let {

                with(res) {
                    header("Content-Type", "application/json")
                    status(200)
                }
                it
            } ?: run {
                res.status(404)
                "Seller not found"
            }
        }, JsonResponseTransformer())
    }

    fun Any.toJSON(): String {
        return JsonSupport.apiObjectMapper.writeValueAsString(this)
    }

    class JsonResponseTransformer : ResponseTransformer {
        override fun render(model: Any?): String = JsonSupport.apiObjectMapper.writeValueAsString(model)
    }


}