package kamon.demo.tracing.user.api

import cats.effect.IO
import org.http4s.client.Client
import io.circe.syntax._
import kamon.demo.tracing.user.model.Filter
import kamon.demo.tracing.user.program.{ItemProgram, SearchProgram}
import kamon.demo.tracing.user.utils.LogSupport
import org.http4s._
import org.http4s.dsl.io._

case class SearchService(client: Client[IO]) extends LogSupport {
  import kamon.demo.tracing.user.utils.CirceUtils.circeCustomSyntax._
  import SearchService._

  def service(searchProgram: SearchProgram, itemProgram: ItemProgram): HttpService[IO] = {

    def route: HttpService[IO] = HttpService[IO] {
      case GET -> Root :? KeyWordParam(keyWord) => handleGetByKeyWord(keyWord)
      case GET -> Root / "details" / LongVar(itemId) / LongVar(userId)
        :? InParallelParam(parallel) => handleGetItemDetails(itemId, userId, parallel.getOrElse(false))
    }

    def handleGetByKeyWord(keyWord: Option[String]): IO[Response[IO]] =
      searchProgram.search(Filter(keyWord)).attempt.flatMap {
        case Right(result) => Ok(result.asJson)
        case Left(cause) =>
          val message = s"Error searching for keyWord '$keyWord'. Cause: ${cause.getMessage}"
          log.error(message, cause)
          InternalServerError(message)
      }

    def handleGetItemDetails(itemId: Long, userId: Long, parallel: Boolean): IO[Response[IO]] = {
      val details = if (parallel) {
        itemProgram.detailsInParallel(itemId, userId: Long)
      } else {
        itemProgram.details(itemId, userId: Long)
      }
      details.attempt.flatMap {
        case Right(result) => Ok(result.asJson)
        case Left(cause) =>
          val message = s"Error getting item details for [ItemId = $itemId, userId: $userId]. Cause: ${cause.getMessage}"
          log.error(message, cause)
          InternalServerError(message)
      }
    }

    route
  }
}

object SearchService {

  case object KeyWordParam extends OptionalQueryParamDecoderMatcher[String]("key-word")
  case object InParallelParam extends OptionalQueryParamDecoderMatcher[Boolean]("parallel")
}
