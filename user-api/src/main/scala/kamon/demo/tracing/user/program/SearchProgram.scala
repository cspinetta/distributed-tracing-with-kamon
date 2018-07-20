package kamon.demo.tracing.user.program

import cats.effect.IO
import kamon.demo.tracing.user.client.InternalProviderClient
import kamon.demo.tracing.user.model.{Filter, Item}

class SearchProgram(internalProviderClient: InternalProviderClient) {

  def search(filter: Filter): IO[List[Item]] = {
    internalProviderClient.searchByFilter(filter)
  }

}
