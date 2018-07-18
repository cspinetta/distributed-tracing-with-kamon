package kamon.demo.tracing.search.program

import cats.effect.IO
import kamon.demo.tracing.search.client.InternalProviderClient
import kamon.demo.tracing.search.model.{Filter, Item}

class SearchProgram(internalProviderClient: InternalProviderClient,
                    externalProviderClient: InternalProviderClient) {

  def search(filter: Filter): IO[Either[Throwable, List[Item]]] = {
    internalProviderClient.searchByFilter(filter)
  }

}
