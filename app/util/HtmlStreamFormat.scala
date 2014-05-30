package util

import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.{Concurrent, Enumeratee, Enumerator}
import play.api.templates.{HtmlFormat, Html}
import play.templates.{Format, Appendable}
import rx.lang.scala.{Observer, Observable}

import scala.concurrent.ExecutionContext


case class HtmlObservable(obs: Observable[Html]) extends Appendable[HtmlObservable] {
  def +=(other: HtmlObservable): HtmlObservable = HtmlObservable(obs ++ other.obs)
}

object HtmlObservable {
  def apply(text: String): HtmlObservable = apply(Html(text))
  def apply(html: Html): HtmlObservable = apply(Observable.items(html))
  def apply(observables: Observable[Html]*): HtmlObservable = apply(observables reduceLeft (_ merge _))
}

object HtmlObservableFormat extends Format[HtmlObservable] {
  def raw(text: String): HtmlObservable = HtmlObservable(text)
  def escape(text: String): HtmlObservable = HtmlObservable(HtmlFormat.escape(text))
}

object ImplicitConversions {
  import language.implicitConversions

  private final class HtmlChannelObserver(chan: Channel[Html]) extends Observer[Html] {
    override def onNext(arg: Html) = chan.push(arg)
    override def onCompleted() = chan.end()
    override def onError(e: Throwable) = chan.end(e)
  }

  implicit def asEnumerator(stream: HtmlObservable)(implicit ec: ExecutionContext): Enumerator[Html] = {
    val enum = Concurrent.unicast[Html] { chan => stream.obs.subscribe(new HtmlChannelObserver(chan)) }
    enum &> Enumeratee.filterNot(_.body.isEmpty)
  }
}
