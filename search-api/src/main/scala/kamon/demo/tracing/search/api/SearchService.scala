package kamon.demo.tracing.search.api

import cats.effect.IO
import org.http4s.client.Client
import io.circe.syntax._
import kamon.demo.tracing.search.model.Filter
import kamon.demo.tracing.search.program.{ItemProgram, SearchProgram}
import kamon.demo.tracing.search.utils.LogSupport
import org.http4s._
import org.http4s.dsl.io._

case class SearchService(client: Client[IO]) extends LogSupport {
  import kamon.demo.tracing.search.utils.CirceUtils.circeCustomSyntax._
  import SearchService._

  def service(searchProgram: SearchProgram, itemProgram: ItemProgram): HttpService[IO] = {

    def route: HttpService[IO] = HttpService[IO] {
      case GET -> Root :? KeyWordParam(keyWord) => handleGetByKeyWord(keyWord)
      case GET -> Root / "details" / LongVar(itemId) / LongVar(userId) => handleGetItemDetails(itemId, userId)
    }

    def handleGetByKeyWord(keyWord: Option[String]): IO[Response[IO]] =
      searchProgram.search(Filter(keyWord)).attempt.flatMap {
        case Right(result) => Ok(result.asJson)
        case Left(cause) =>
          val message = s"Error searching for keyWord '$keyWord'. Cause: ${cause.getMessage}"
          log.error(message, cause)
          InternalServerError(message)
      }

    def handleGetItemDetails(itemId: Long, userId: Long): IO[Response[IO]] =
      itemProgram.details(itemId, userId: Long).attempt.flatMap {
        case Right(result) => Ok(result.asJson)
        case Left(cause) =>
          val message = s"Error getting item details for [ItemId = $itemId, userId: $userId]. Cause: ${cause.getMessage}"
          log.error(message, cause)
          InternalServerError(message)
      }

    route
  }
}

object SearchService {

  case object KeyWordParam extends OptionalQueryParamDecoderMatcher[String]("key-word")
}
