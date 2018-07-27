package kamon.demo.tracing.users.program

import cats.effect.IO
import kamon.Kamon
import kamon.demo.tracing.users.utils.LogSupport
import kamon.executors.util.ContextAwareExecutorService
import kamon.trace.Span

import scala.util.Random

class StatsProgram(ec: ContextAwareExecutorService) extends LogSupport {

  val r: Random = scala.util.Random

  def generateStats(userId: Long): IO[Unit] = IO.pure {
    ec.execute(() => {
      log.info(s"Calculating stats for user $userId")
      val statsExecutionSpan = Kamon.buildSpan("stats.execution")
        .asChildOf(Kamon.currentContext().get(Span.ContextKey))
        .withMetricTag("span.kind", "cpu-intensive-operation")
        .withTag("user.id", userId)
        .start()
      Kamon.withContext(Kamon.currentContext().withKey(Span.ContextKey, statsExecutionSpan)) {
        Thread.sleep(r.nextInt(StatsProgram.MaxTimeForStepInStats.toMillis.toInt))
        Kamon.currentSpan().mark("Step 1: .... This is free text!")
        Thread.sleep(r.nextInt(StatsProgram.MaxTimeForStepInStats.toMillis.toInt))
        Kamon.currentSpan().mark("Step 2: .... This is free text!")
        Thread.sleep(r.nextInt(StatsProgram.MaxTimeForStepInStats.toMillis.toInt))
        Kamon.currentSpan().mark("Step 3: .... This is free text!")
        Thread.sleep(r.nextInt(StatsProgram.MaxTimeForStepInStats.toMillis.toInt))
        Kamon.currentSpan().mark("Step 4: .... This is free text!")
        Thread.sleep(r.nextInt(StatsProgram.MaxTimeForStepInStats.toMillis.toInt))
        Kamon.currentSpan().mark("Step 5: .... This is free text!")
        Kamon.currentSpan().finish()
      }
    })
  }
}

object StatsProgram {
  import scala.concurrent.duration._

  val MaxTimeForStepInStats: FiniteDuration = 1.seconds
}
