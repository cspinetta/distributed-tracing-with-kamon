package kamon.demo.tracing.search.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

object TimeCodecs {

  implicit val LocalDateFormat: Encoder[LocalDate] with Decoder[LocalDate] = new Encoder[LocalDate] with Decoder[LocalDate] {
    override def apply(a: LocalDate): Json = Encoder.encodeString.apply(a.format(DateTimeFormatter.ISO_DATE))
    override def apply(c: HCursor): Result[LocalDate] = Decoder.decodeString.map(s => LocalDate.parse(s)).apply(c)
  }
}
