package kamon.demo.tracing.front.repository

import javax.inject.{Inject, Singleton}
import kamon.demo.tracing.front.base.Config.ServiceConfig
import kamon.demo.tracing.front.base.{ConfigSupport, LogSupport, RestClient}
import kamon.demo.tracing.front.model.User
import play.api.MarkerContext
import play.api.libs.ws._

import scala.concurrent.Future


@Singleton
class UserRepository @Inject() (ws: WSClient)(implicit ec: ItemsExecutionContext)
  extends RestClient with ConfigSupport with LogSupport {

  val c: ServiceConfig.UserApi = config.service.userApi

  def findById(userId: Long)(implicit mc: MarkerContext): Future[User] = {
    log.trace(s"get: userId = $userId")
    val r = ws
      .get(s"${c.host}${c.endpoint}/$userId")
      .withRequestTimeout(c.timeout)
      .execute()
      .handleSync[User](c.timeout)
    Future.fromTry(r)
  }
}
