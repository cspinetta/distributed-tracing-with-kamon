package kamon.demo.tracing.seller

import kamon.demo.tracing.seller.base.JsonSupport
import kamon.demo.tracing.seller.repository.SellerRepository
import mu.KLogging
import spark.Request
import spark.Response
import spark.ResponseTransformer
import spark.Route
import spark.Spark.*

object Routes : KLogging() {

    fun init(sellerRepository: SellerRepository) {

        before("/*") { req, _ -> logger.info("Request: ${req.pathInfo()}") }

        get("/api/seller/:id", Route { req: Request, res: Response ->
            val id: Long = req.params(":id").toLong()
            sellerRepository.findById(id)?.let {

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