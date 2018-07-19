package kamon.demo.tracing.search.program

import cats.effect.IO
import kamon.demo.tracing.search.client.InternalProviderClient
import kamon.demo.tracing.search.model.{Filter, Item}

class SearchProgram(internalProviderClient: InternalProviderClient) {

  def search(filter: Filter): IO[List[Item]] = {
    internalProviderClient.searchByFilter(filter)
  }

}
