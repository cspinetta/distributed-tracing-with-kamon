package kamon.demo.tracing.front.service

import javax.inject.{Inject, Singleton}
import kamon.demo.tracing.front.model.{Item, ItemDetails, User}
import kamon.demo.tracing.front.repository.{ItemsRepository, UserRepository}
import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}


/**
  * Controls access to the backend data, returning PostResource
  */
@Singleton
class ItemsService @Inject()(itemsRepository: ItemsRepository, userRepository: UserRepository)(implicit ec: ExecutionContext) {

  def findByFilter(filter: ItemsFilter, userId: Long)(implicit mc: MarkerContext): Future[ItemsResult] = {
    for {
      items <- itemsRepository.search(filter)
      user <- userRepository.findById(userId)
    } yield ItemsResult(items = items, user = user)
  }

  def details(itemId: Long, userId: Long)(implicit mc: MarkerContext): Future[ItemDetails] = {
    for {
      itemDetails <- itemsRepository.details(itemId)
      user <- userRepository.findById(userId)
    } yield ItemDetails(item = itemDetails.item, seller = itemDetails.seller, user = user)
  }
}

@Singleton
class FixItemsService @Inject()(itemsRepository: ItemsRepository, userRepository: UserRepository)(implicit ec: ExecutionContext) {

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

case class ItemsFilter(keyWord: Option[String])
case class ItemsResult(items: List[Item], user: User)
