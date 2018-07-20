package kamon.demo.tracing.front.controllers

import javax.inject.Inject
import kamon.demo.tracing.front.base.LogSupport
import kamon.demo.tracing.front.base.serializer.SnakeCaseSerializer
import kamon.demo.tracing.front.model.User
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Takes HTTP requests and produces JSON.
  */
class UserController @Inject()(cc: ApiControllerComponents)(implicit ec: ExecutionContext)
    extends ApiBaseController(cc) with LogSupport {


  def user(userId: Long): Action[AnyContent] = ApiAction.async { implicit request =>
    Future.successful(Ok(SnakeCaseSerializer.write(User(id = userId, email = s"user-$userId@domain.com"))))
  }
}
