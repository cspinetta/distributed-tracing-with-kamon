package kamon.demo.tracing.search.client

import cats.effect.IO
import kamon.demo.tracing.search.conf.ConfigSupport
import kamon.demo.tracing.search.model.{Filter, Item}
import org.http4s.Uri
import org.http4s.client.Client
import kamon.demo.tracing.search.utils._

class ExternalProviderClient(client: Client[IO]) extends ConfigSupport {
  import UriUtils._
  import Item._
  import CirceUtils.circeCustomSyntax._
  import ExternalProviderClient._

  private val c = config.service.externalProvider
  private lazy val uriPrototype = Uri.fromString(s"${c.host}${c.endpoint}")

  def searchByFiler(filter: Filter): IO[Result[List[Item]]] = {
    val uri = uriPrototype.map(_
      .withOptionQueryParam("key-word", filter.keyWord)
//      .withOptionQueryParam("category", filter.category)
//      .withOptionQueryParam("seller", filter.seller)
    ).parse
    client.expect(uri)(jsonOf[IO, List[Item]]).attempt
  }

  def searchByHotSale(categories: List[String]): IO[Result[List[Item]]] = {
    val uri = uriPrototype.map(_
      .withPath("/hot-sale")
      .withQueryParam("category", categories)
    ).parse
    client.expect(uri)(jsonOf[IO, List[Item]]).attempt
  }
}

object ExternalProviderClient {
  type Result[T] = Either[Throwable, T]
}
