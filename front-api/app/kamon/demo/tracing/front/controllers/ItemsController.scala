package kamon.demo.tracing.front.controllers

import javax.inject.Inject
import kamon.demo.tracing.front.base.LogSupport
import kamon.demo.tracing.front.base.serializer.SnakeCaseSerializer
import kamon.demo.tracing.front.service.ItemsFilter
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext

/**
  * Takes HTTP requests and produces JSON.
  */
class ItemsController @Inject()(cc: ApiControllerComponents)(implicit ec: ExecutionContext)
    extends ApiBaseController(cc) with LogSupport {


//  private val form: Form[PostFormInput] = {
//    import play.api.data.Forms._
//
//    Form(
//      mapping(
//        "title" -> nonEmptyText,
//        "body" -> text
//      )(PostFormInput.apply)(PostFormInput.unapply)
//    )
//  }

//  def index: Action[AnyContent] = PostAction.async { implicit request =>
//    logger.trace("index: ")
//    postResourceHandler.find.map { posts =>
//      Ok(Json.toJson(posts))
//    }
//  }

//  def process: Action[AnyContent] = PostAction.async { implicit request =>
//    logger.trace("process: ")
//    processJsonPost()
//  }

  def search: Action[AnyContent] = ApiAction.async { implicit request =>
    val keyWord = request.getQueryString("key-word")
    log.trace(s"search: wordKey = $keyWord")
    itemsService.findByFilter(ItemsFilter(keyWord)).map { items =>
      Ok(SnakeCaseSerializer.write(items))
    }
  }

  def details(itemId: Long, userId: Long): Action[AnyContent] = ApiAction.async { implicit request =>
    itemsService.details(itemId, userId).map { itemDetails =>
      Ok(SnakeCaseSerializer.write(itemDetails))
    }
  }

//  private def processJsonPost[A]()(implicit request: PostRequest[A]): Future[Result] = {
//    def failure(badForm: Form[PostFormInput]) = {
//      Future.successful(BadRequest(badForm.errorsAsJson))
//    }
//
//    def success(input: PostFormInput) = {
//      postResourceHandler.create(input).map { post =>
//        Created(Json.toJson(post)).withHeaders(LOCATION -> post.link)
//      }
//    }
//
//    form.bindFromRequest().fold(failure, success)
//  }
}
