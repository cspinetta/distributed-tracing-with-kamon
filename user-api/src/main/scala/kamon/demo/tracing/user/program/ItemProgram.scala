package kamon.demo.tracing.user.program

import cats.effect.IO
import kamon.demo.tracing.user.client.{InternalProviderClient, UserClient}
import kamon.demo.tracing.user.model.ItemDetails

class ItemProgram(internalProviderClient: InternalProviderClient,
                  userClient: UserClient) {

  def details(itemId: Long, userId: Long): IO[ItemDetails] = {
    for {
      itemDetails <- internalProviderClient.detailsById(itemId)
      user <- userClient.findById(userId)
    } yield {
      ItemDetails(itemDetails.item, itemDetails.seller, user)
    }
  }
  def detailsInParallel(itemId: Long, userId: Long): IO[ItemDetails] = {
    import cats.syntax.parallel._
    (internalProviderClient.detailsById(itemId), userClient.findById(userId))
      .parMapN((itemDetails, user) => ItemDetails(itemDetails.item, itemDetails.seller, user))
  }

}
