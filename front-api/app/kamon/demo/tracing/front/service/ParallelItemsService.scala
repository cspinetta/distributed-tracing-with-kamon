package kamon.demo.tracing.front.service

import javax.inject.{Inject, Singleton}
import kamon.demo.tracing.front.model.ItemDetails
import kamon.demo.tracing.front.repository.{ItemsRepository, UsersRepository}
import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ParallelItemsService @Inject()(itemsRepository: ItemsRepository, userRepository: UsersRepository)(implicit ec: ExecutionContext) {

  def findByFilter(filter: ItemsFilter, userId: Long)(implicit mc: MarkerContext): Future[ItemsResult] = {
    val itemsFuture = itemsRepository.search(filter)
    val userFuture = userRepository.findById(userId)
    for {
      items <- itemsFuture
      user <- userFuture
    } yield ItemsResult(items = items, user = user)
  }

  def details(itemId: Long, userId: Long)(implicit mc: MarkerContext): Future[ItemDetails] = {
    val itemDetailsFuture = itemsRepository.details(itemId)
    val userFuture = userRepository.findById(userId)
    for {
      itemDetails <- itemDetailsFuture
      user <- userFuture
    } yield ItemDetails(item = itemDetails.item, seller = itemDetails.seller, user = user)
  }
}
