package kamon.demo.tracing.search.client

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.semiauto._
import kamon.demo.tracing.search.conf.ConfigSupport
import kamon.demo.tracing.search.model.{Filter, Item}
import kamon.demo.tracing.search.utils._
import org.http4s.Uri
import org.http4s.client.Client

class InternalProviderClient(client: Client[IO]) extends ConfigSupport {
  import CirceUtils.circeCustomSyntax._
  import InternalProviderClient._
  import UriUtils._

  private val c = config.service.internalProvider
  private lazy val uriPrototype = Uri.fromString(s"${c.host}${c.endpoint}")

  def searchByFilter(filter: Filter): IO[Result[List[Item]]] = {
    val uri = uriPrototype.map(uri =>
      (uri / "search")
      .withOptionQueryParam("key-word", filter.keyWord)
    ).parse
    client.expect(uri)(jsonOf[IO, Response]).attempt.map(parseResult)
  }

  def searchByHotSale(categories: List[String]): IO[Result[List[Item]]] = {
    val uri = uriPrototype.map(_
      .withPath("/hot-sale")
      .withQueryParam("category", categories)
    ).parse
    client.expect(uri)(jsonOf[IO, Response]).attempt.map(parseResult)
  }

  def parseResult(response: Result[Response]): Result[List[Item]] =
    response.map(_.content)

}

object InternalProviderClient {

  type Result[T] = Either[Throwable, T]

  case class Response(content: List[Item])

  object Response {
    import Item._

    implicit val encoder: Encoder[Response] = deriveEncoder
    implicit val decoder: Decoder[Response] = deriveDecoder
  }
}
