package kamon.demo.tracing.front.base.serializer

trait Serializer {

  def read[T](value: String)(implicit mf: Manifest[T]): T

  def write(obj: Any): String

}
