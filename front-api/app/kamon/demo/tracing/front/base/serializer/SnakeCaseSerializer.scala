package kamon.demo.tracing.front.base.serializer

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.{JsonInclude, PropertyAccessor}
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import play.api.libs.Jsonp
import play.api.libs.json.jackson.PlayJsonModule

//class JavaJsonCustomObjectMapper() {
//  val mapper: Nothing = Jsonp.newDefaultMapper.enable // enable features and customize the object mapper here ...
//  DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//  // etc.
//  Json.setObjectMapper(mapper)
//}

object SnakeCaseSerializer extends SnakeCaseSerializer(Nil)

class SnakeCaseSerializer(customModules:List[Module]) extends Serializer {

  private val objectMapper: ObjectMapper with ScalaObjectMapper = {
    val m = new ObjectMapper() with ScalaObjectMapper
    m.registerModule(new DefaultScalaModule)
    m.registerModule(PlayJsonModule)
    m.registerModules(customModules:_*)
    m.registerModule(new Jdk8Module())
    m.registerModule(new JavaTimeModule())
    m.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    m.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
    m.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
    m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    m.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    m.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
    m.setVisibility(PropertyAccessor.ALL, Visibility.NONE)
    m.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
    m
  }

  def write(obj:Any):String = objectMapper.writeValueAsString(obj)

  def read[T](json:String)(implicit mf: Manifest[T]):T = objectMapper.readValue[T](json)

}










