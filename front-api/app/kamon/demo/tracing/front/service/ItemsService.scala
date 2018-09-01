package kamon.demo.tracing.front.service

import javax.inject.{Inject, Singleton}
import kamon.demo.tracing.front.model.{Item, ItemDetails, User}
import kamon.demo.tracing.front.repository.{ItemsRepository, UsersRepository}
import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ItemsService @Inject()(itemsRepository: ItemsRepository, usersRepository: UsersRepository)(implicit ec: ExecutionContext) {

  def findByFilter(filter: ItemsFilter, userId: Long)(implicit mc: MarkerContext): Future[ItemsResult] = {
    itemsRepository.search(filter)
      .flatMap(items => usersRepository.findById(userId)
        .map(user => ItemsResult(items = items, user = user)))
  }

  def details(itemId: Long, userId: Long)(implicit mc: MarkerContext): Future[ItemDetails] = {
    for {
      itemDetails <- itemsRepository.details(itemId)
      user <- usersRepository.findById(userId)
    } yield ItemDetails(item = itemDetails.item, seller = itemDetails.seller, user = user)
  }
}

case class ItemsFilter(keyWord: Option[String])
case class ItemsResult(items: List[Item], user: User)
