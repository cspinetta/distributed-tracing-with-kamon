package kamon.demo.tracing.front.repository

import akka.actor.ActorSystem
import javax.inject.{Inject, Singleton}
import kamon.demo.tracing.front.base.Config.ServiceConfig
import kamon.demo.tracing.front.base.{ConfigSupport, LogSupport, RestClient}
import kamon.demo.tracing.front.model.{Item, Seller}
import kamon.demo.tracing.front.service.ItemsFilter
import play.api.MarkerContext
import play.api.libs.concurrent.CustomExecutionContext
import play.api.libs.ws._

import scala.concurrent.Future

case class ItemsResponse(content: List[Item])
case class ItemDetailsResponse(item: Item, seller: Option[Seller])

class ItemsExecutionContext @Inject()(actorSystem: ActorSystem) extends CustomExecutionContext(actorSystem, "repository.dispatcher")

@Singleton
class ItemsRepository @Inject() (ws: WSClient)(implicit ec: ItemsExecutionContext)
  extends RestClient with ConfigSupport with LogSupport {

  val c: ServiceConfig.ItemApi = config.service.itemApi

  def search(filter: ItemsFilter)(implicit mc: MarkerContext): Future[List[Item]] = {
      log.trace(s"get: filter = $filter")
    ws
      .get(s"${c.host}${c.endpoint}/search")
      .addQueryStringParameters("key-word" -> filter.keyWord.getOrElse(""))
      .withRequestTimeout(c.timeout)
      .execute()
      .handleAsync[ItemsResponse]
      .map(_.content)
  }

  def details(itemId: Long)(implicit mc: MarkerContext): Future[ItemDetailsResponse] = {
    log.trace(s"details: itemId = $itemId")
    ws
      .get(s"${c.host}${c.endpoint}/$itemId/details")
      .withRequestTimeout(c.timeout)
      .execute()
      .handleAsync[ItemDetailsResponse]
  }
}
