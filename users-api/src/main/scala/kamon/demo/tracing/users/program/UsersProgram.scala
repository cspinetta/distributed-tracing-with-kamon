package kamon.demo.tracing.users.program

import cats.effect.IO
import kamon.demo.tracing.users.model.User

class UsersProgram(statsProgram: StatsProgram) {

  def findById(userId: Long): IO[Option[User]] = {
    if (userId <= 100) retrieveUserById(userId).map(Some.apply)
    else               IO.pure(None)
  }

  private val retrieveUserById: Long => IO[User] = (userId: Long) => {
    updateStats(userId).map { _ =>
      User(id = userId, email = s"user-$userId@domain.com")
    }
  }

  private def updateStats(userId: Long): IO[Unit] = {
    if (userId > 50) statsProgram.generateStats(userId)
    else             IO.pure(())
  }
}
