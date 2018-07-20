package kamon.demo.tracing.user.program

import cats.effect.IO
import kamon.demo.tracing.user.client.StatsClient
import kamon.demo.tracing.user.model.User

import scala.util.Random

class UserProgram(statsClient: StatsClient) {

  val r: Random = scala.util.Random

  def findById(userId: Long): IO[Option[User]] = {
    if (userId <= 100) retrieveUserById(userId).map(Some.apply)
    else               IO.pure(None)
  }

  private val retrieveUserById: Long => IO[User] = (userId: Long) => {
    for {
      _ <- if (haveToInform) statsClient.informUser(userId) else IO.pure(())
    } yield {
      User(id = userId, email = s"user-$userId@domain.com")
    }
  }

  private def haveToInform: Boolean = r.nextBoolean()
}
