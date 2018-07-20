package kamon.demo.tracing.front.controllers

import javax.inject.Inject
import kamon.demo.tracing.front.service.{ItemsService, ParallelItemsService}
import net.logstash.logback.marker.LogstashMarker
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.libs.concurrent.Futures
import play.api.libs.concurrent.Futures._
import play.api.mvc._
import play.api.{Logger, MarkerContext}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal
import scala.util.{Failure, Success}


trait ApiRequestHeader extends MessagesRequestHeader with PreferredMessagesProvider
class ApiRequest[A](request: Request[A], val messagesApi: MessagesApi) extends WrappedRequest(request) with ApiRequestHeader

trait RequestMarkerContext {
  import net.logstash.logback.marker.Markers

  private def marker(tuple: (String, Any)) = Markers.append(tuple._1, tuple._2)

  private implicit class RichLogstashMarker(marker1: LogstashMarker) {
    def &&(marker2: LogstashMarker): LogstashMarker = marker1.and(marker2)
  }

  implicit def requestHeaderToMarkerContext(implicit request: RequestHeader): MarkerContext = {
    MarkerContext {
      marker("id" -> request.id) && marker("host" -> request.host) && marker("remoteAddress" -> request.remoteAddress)
    }
  }

}

class ApiActionBuilder @Inject()(messagesApi: MessagesApi, playBodyParsers: PlayBodyParsers)
                                (implicit val executionContext: ExecutionContext, futures: Futures)
    extends ActionBuilder[ApiRequest, AnyContent]
    with RequestMarkerContext
    with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  type PostRequestBlock[A] = ApiRequest[A] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A],
                              block: PostRequestBlock[A]): Future[Result] = {

    // Convert to marker context and use request in block
    implicit val markerContext: MarkerContext = requestHeaderToMarkerContext(request)
    logger.info(s"Request: ${request.uri} [${request.method}]")

    block(new ApiRequest(request, messagesApi))
      .transform {
        case Success(result) =>
          Success((request.method match {
            case GET | HEAD =>
              result.withHeaders("Cache-Control" -> s"max-age: 100")
            case _ =>
              result
          }).as("application/json"))
        case Failure(NonFatal(exception)) =>
          logger.error(s"Error occurs trying to response to ${request.uri}", exception)
          Success(Results.InternalServerError(s"We failed due to an internal error. Cause: ${exception.getMessage}"))
      }
      .withTimeout(20.seconds)
  }
}

case class ApiControllerComponents @Inject()(apiActionBuilder: ApiActionBuilder,
                                             itemsService: ItemsService,
                                             parallelItemsService: ParallelItemsService,
                                             actionBuilder: DefaultActionBuilder,
                                             parsers: PlayBodyParsers,
                                             messagesApi: MessagesApi,
                                             langs: Langs,
                                             fileMimeTypes: FileMimeTypes,
                                             executionContext: scala.concurrent.ExecutionContext)
  extends ControllerComponents

class ApiBaseController @Inject()(pcc: ApiControllerComponents) extends BaseController with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = pcc

  def ApiAction: ApiActionBuilder = pcc.apiActionBuilder

  def itemsService: ItemsService = pcc.itemsService
  def parallelItemsService: ParallelItemsService = pcc.parallelItemsService
}