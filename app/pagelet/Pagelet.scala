package pagelet

import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.{Concurrent, Enumeratee, Enumerator}
import play.api.libs.json.{JsString, Json}
import play.twirl.api.Html
import rx.lang.scala.{Observable, Observer}

import scala.concurrent.ExecutionContext

/**
 * A simple container for an Html to have the type system reflect, when we deal with pagelets and when with regular html
 * @param html the html representaiton of this pagelet
 */
case class Pagelet(html: Html)

/**
 * Helper methods that render single pagelets or combines multiple ones.
 */
object Pagelet {

  private final class HtmlChannelObserver(chan: Channel[Html]) extends Observer[Pagelet] {
    override def onNext(arg: Pagelet) = chan.push(arg.html)
    override def onCompleted() = chan.end()
    override def onError(e: Throwable) = chan.end(e)
  }

  /**
   * converts an PageletObservable to an Enumerator[Html].
   * This translated from the reactive RxJava world into the reactive Play world.
   */
  private def asEnumerator(stream: PageletObservable)(implicit ec: ExecutionContext): Enumerator[Html] = {
    val enum = Concurrent.unicast[Html] { chan => stream.obs.subscribe(new HtmlChannelObserver(chan)) }
    enum &> Enumeratee.filterNot(_.body.isEmpty)
  }

  /**
   * render a single pagelet
   * @param html  the html of the pagelet's content
   * @param id    the ID of the placeholder element in the layout
   * @param js    a list of JavaScript resources for this pagelet
   * @param css   a list of CSS resources for this pagelet
   * @return  the html for the pagelet
   */
  def render(html: Html, id: String, js: List[String], css: List[String]): Pagelet = {
    val jsJs = js.map(j => Json.stringify(JsString(j)))
    val cssJs = css.map(c => Json.stringify(JsString(c)))
    Pagelet(views.html.pagelet.pagelet(html, id, jsJs, cssJs))
  }

  /**
   * enumerates several observables of pagelet
   * @param observables a list of pagelets that should be streamed
   * @param view  a .stream template that is the layout for the individual chunks
   * @param ec    the execution context
   * @return an Enumerator[Html] that can be streamed by e.g. Ok.chunked()
   */
  def enumerate(observables: Observable[Pagelet]*)(view: PageletObservable => PageletObservable)(implicit ec: ExecutionContext): Enumerator[Html] = {
    val mergedObservable = observables reduceLeft (_ merge _)
    val htmlObservable = PageletObservable(mergedObservable)
    val htmlStream = view(htmlObservable)
    asEnumerator(htmlStream)
  }

}
