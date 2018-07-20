package kamon.demo.tracing.user.utils

import org.http4s.{ParseResult, Uri}

object UriUtils {

  implicit class UriParser(val parsedUri: ParseResult[Uri]) extends AnyVal {
    def parse: Uri = {
      parsedUri
        .left.map(cause => throw new RuntimeException("Impossible to create a URI", cause))
        .right.get
    }
  }
}
