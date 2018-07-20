package kamon.demo.tracing.front.controllers

import javax.inject.Inject
import kamon.demo.tracing.front.base.LogSupport
import kamon.demo.tracing.front.base.serializer.SnakeCaseSerializer
import kamon.demo.tracing.front.service.ItemsFilter
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext

class ItemsController @Inject()(cc: ApiControllerComponents)(implicit ec: ExecutionContext)
    extends ApiBaseController(cc) with LogSupport {

  def search: Action[AnyContent] = ApiAction.async { implicit request =>
    val keyWord = request.getQueryString("key-word")
    itemsService.findByFilter(ItemsFilter(keyWord), userId = 32).map { itemsResult =>
      Ok(SnakeCaseSerializer.write(itemsResult))
    }
  }

  def details(itemId: Long, userId: Long): Action[AnyContent] = ApiAction.async { implicit request =>
    itemsService.details(itemId, userId).map { itemDetails =>
      Ok(SnakeCaseSerializer.write(itemDetails))
    }
  }
}

class FixItemsController @Inject()(cc: ApiControllerComponents)(implicit ec: ExecutionContext)
    extends ApiBaseController(cc) with LogSupport {

  def search: Action[AnyContent] = ApiAction.async { implicit request =>
    val keyWord = request.getQueryString("key-word")
    fixItemsService.findByFilter(ItemsFilter(keyWord), userId = 32).map { itemsResult =>
      Ok(SnakeCaseSerializer.write(itemsResult))
    }
  }

  def details(itemId: Long, userId: Long): Action[AnyContent] = ApiAction.async { implicit request =>
    fixItemsService.details(itemId, userId).map { itemDetails =>
      Ok(SnakeCaseSerializer.write(itemDetails))
    }
  }
}
