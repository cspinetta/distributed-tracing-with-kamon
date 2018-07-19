package kamon.demo.tracing.search.program

import cats.effect.IO
import kamon.demo.tracing.search.client.{InternalProviderClient, UserClient}
import kamon.demo.tracing.search.model.ItemDetails

class ItemProgram(internalProviderClient: InternalProviderClient,
                  userClient: UserClient) {

  def details(itemId: Long, userId: Long): IO[ItemDetails] = {
    for {
      itemDetais <- internalProviderClient.detailsById(itemId)
      user <- userClient.findById(userId)
    } yield {
      ItemDetails(itemDetais.item, itemDetais.seller, user)
    }
  }

}
