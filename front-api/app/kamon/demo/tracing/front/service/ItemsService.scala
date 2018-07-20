package kamon.demo.tracing.front.service

import javax.inject.{Inject, Singleton}
import kamon.demo.tracing.front.model.{Item, ItemDetails}
import kamon.demo.tracing.front.repository.{ItemsRepository, UserRepository}
import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}


/**
  * Controls access to the backend data, returning PostResource
  */
@Singleton
class ItemsService @Inject()(itemsRepository: ItemsRepository, userRepository: UserRepository)(implicit ec: ExecutionContext) {

  def findByFilter(filter: ItemsFilter)(implicit mc: MarkerContext): Future[List[Item]] = {
    itemsRepository.search(filter)
  }

  def details(itemId: Long, userId: Long)(implicit mc: MarkerContext): Future[ItemDetails] = {
    for {
      itemDetails <- itemsRepository.details(itemId)
      user <- userRepository.findById(userId)
    } yield ItemDetails(item = itemDetails.item, seller = itemDetails.seller, user = user)
  }
}

case class ItemsFilter(keyWord: Option[String])
