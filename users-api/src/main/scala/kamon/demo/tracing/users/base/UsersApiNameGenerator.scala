package kamon.demo.tracing.users.base

import kamon.http4s.NameGenerator
import org.http4s.Request

final class UsersApiNameGenerator extends NameGenerator {

  import java.util.Locale

  import scala.collection.concurrent.TrieMap

  private val localCache = TrieMap.empty[String, String]
  private val normalizePattern = """\/(\d+)""".r

  override def generateHttpClientOperationName[F[_]](request: Request[F]): String = {
    request.uri.authority.map(authority => s"$authority${request.uri.path}").getOrElse(request.uri.path)
  }

  override def generateOperationName[F[_]](request: Request[F]): String = {
    localCache.getOrElseUpdate(s"${request.method.name}${request.uri.path}", {
      // Convert paths of form GET /foo/bar/$paramname<regexp>/blah to foo.bar.paramname.blah.get
      val uri = request.uri
      val p = normalizePattern.replaceAllIn(uri.path, "/#").replace('/', '.').dropWhile(_ == '.')
      val normalisedPath = {
        if (p.lastOption.exists(_ != '.')) s"$p."
        else p
      }
      s"$normalisedPath${request.method.name.toLowerCase(Locale.ENGLISH)}"
    })
  }
}
