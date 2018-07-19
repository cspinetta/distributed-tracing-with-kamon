package kamon.demo.tracing.search.client

import cats.effect.IO
import io.circe.generic.extras.ConfiguredJsonCodec
import kamon.demo.tracing.search.conf.ConfigSupport
import kamon.demo.tracing.search.model.{Filter, Item, Seller}
import kamon.demo.tracing.search.utils._
import org.http4s.Uri
import org.http4s.client.Client

class InternalProviderClient(client: Client[IO]) extends ConfigSupport {
  import CirceUtils.circeCustomSyntax._
  import InternalProviderClient._
  import UriUtils._

  private val c = config.service.itemApi
  private lazy val uriPrototype = Uri.fromString(s"${c.host}${c.endpoint}")

  def searchByFilter(filter: Filter): IO[List[Item]] = {
    val uri = uriPrototype.map(uri =>
      (uri / "search")
      .withOptionQueryParam("key-word", filter.keyWord)).parse
    client
      .expect(uri)(jsonOf[IO, ItemsResponse])
      .map(_.content)
  }

  def detailsById(itemId: Long): IO[ItemDetailsResponse] = {
    val uri = uriPrototype.map(uri => uri / s"$itemId" / "details").parse
    client.expect(uri)(jsonOf[IO, ItemDetailsResponse])
  }

}

object InternalProviderClient {
  type Result[T] = Either[Throwable, T]

  @ConfiguredJsonCodec
  case class ItemsResponse(content: List[Item])
  object ItemDetailsResponse  extends CirceApiConfig

  @ConfiguredJsonCodec
  case class ItemDetailsResponse(item: Item, seller: Seller)
  object ItemsResponse extends CirceApiConfig

}
