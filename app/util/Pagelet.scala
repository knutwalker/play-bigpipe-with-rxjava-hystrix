package util

import play.api.http.HeaderNames
import play.api.libs.iteratee.Iteratee
import play.api.mvc.{Codec, Cookie, Cookies, SimpleResult}
import play.api.templates.Html
import rx.lang.scala.Observable

import scala.collection.immutable.Seq
import scala.concurrent.{ExecutionContext, Future}

object Pagelet {

  def readBody(result: SimpleResult)(implicit codec: Codec, ec: ExecutionContext): Future[Html] =
    result.body.run(Iteratee.consume()).map(bs => Html(new String(bs, codec.charset)))

  def mergeCookies(results: SimpleResult*): Seq[Cookie] =
    StaticContent.mergeHeaderValues(HeaderNames.SET_COOKIE, (Cookies.decode _).andThen(_.toList), results: _*)

  def render(html: Html, id: String): Html = views.html.ui.pagelet(html, id)

  def renderStream[T](obs: Observable[T], view: T => Html): Observable[Html] = obs.map(view)
}
