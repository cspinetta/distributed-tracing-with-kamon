package kamon.demo.tracing.front.base

import play.api.Logger

trait LogSupport {

  val log = Logger(getClass)
}
