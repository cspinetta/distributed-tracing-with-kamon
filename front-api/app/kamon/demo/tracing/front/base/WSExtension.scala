package kamon.demo.tracing.front.base

import kamon.demo.tracing.front.base.WSExtension.{PimpWSClient, PimpWSResponse}
import kamon.demo.tracing.front.base.serializer.SnakeCaseSerializer
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}

import scala.concurrent.duration.{Duration, _}
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.control.{NoStackTrace, NonFatal}
import scala.util.{Failure, Success, Try}

/**
  * From: github:despegar/octopus
  */
trait RestClient extends PimpWSClient with PimpWSResponse

object WSExtension extends LogSupport {
  private val defaultTimeout = 10.seconds

  trait PimpWSClient {

    implicit def toFiniteDuration(duration: java.time.Duration): scala.concurrent.duration.Duration = scala.concurrent.duration.Duration.fromNanos(duration.toNanos)

    implicit def WSSyntax(ws: WSClient) = new {
      private def defaultHeaders =
        Map(
          "X-Client" → "Front-API",
          "Accept-Encoding" → "gzip")
      private def jsonHeader =
        Map("Content-Type" → "application/json")

      def withHeaders(url: String): WSRequest = {
        ws.url(url).withHttpHeaders(defaultHeaders.toList: _*)
      }

      def post(url: String): WSRequest = withHeaders(url).withHttpHeaders(jsonHeader.toList: _*).withMethod("POST")
      def get(url: String): WSRequest = withHeaders(url).withMethod("GET")
      def patch(url: String): WSRequest = withHeaders(url).withHttpHeaders(jsonHeader.toList: _*).withMethod("PATCH")
    }
  }

  trait PimpWSResponse extends LogSupport {

    implicit def WSRequest(wsResponse: Future[WSResponse]) = new {

      def handleAsync[A <: AnyRef](implicit mf: Manifest[A], ec: ExecutionContext): Future[A] = {
        val promise = Promise[A]()

        wsResponse.map { response ⇒
          process[A](response) match {
            case Success(resp)   ⇒ promise success resp
            case Failure(cause)  ⇒ promise failure cause
          }
        }(ec)

        promise.future
      }

      def handleSync[A <: AnyRef](timeout: Duration = defaultTimeout)(implicit mf: Manifest[A]): Try[A] = {
        Try(Await.result(wsResponse, timeout)) match {
          case Success(response)        ⇒ process[A](response)
          case Failure(NonFatal(cause)) ⇒ Failure(cause)
        }
      }

      private def process[A <: AnyRef](response: WSResponse)(implicit mf: Manifest[A]): Try[A] = {
        def withBody(msg: String): String = if (Option(response.body).exists(_.nonEmpty)) s"$msg - ${response.body}" else msg
        response.status match {
          case 200 ⇒
            Try(SnakeCaseSerializer.read[A](response.body))
          case 401 ⇒
            Failure(UnauthorizedException(withBody(s"401 - Unauthorized")))
          case 404 ⇒
            Failure(ResourceNotFoundException(withBody(s"404 - Requested resource has not been found")))
          case 500 ⇒
            Failure(InternalServerErrorException(withBody(s"500 - Internal Server Error")))
          case 503 ⇒
            Failure(ServiceUnavailableException(withBody(s"503 - Server is in maintenance")))
          case _ ⇒
            Failure(new RuntimeException(withBody(s"http response status: ${response.status} - ${response.statusText}")))
        }
      }
    }
  }

  case class UnauthorizedException(message: String) extends RuntimeException(message) with NoStackTrace
  case class ResourceNotFoundException(message: String) extends RuntimeException(message) with NoStackTrace
  case class InternalServerErrorException(message: String) extends RuntimeException(message) with NoStackTrace
  case class ServiceUnavailableException(message: String) extends RuntimeException(message) with NoStackTrace

}

