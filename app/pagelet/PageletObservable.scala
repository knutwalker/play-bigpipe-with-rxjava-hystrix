package pagelet

import play.twirl.api.{Appendable, Html}
import rx.lang.scala.Observable

import scala.collection.immutable.Seq


/**
 * A part of a streamable template. Deletegates to appendable logic to the underlying observable.
 * It uses concat (++) rather than merge because concat maintains the order, which is relevant for the templating.
 */
case class PageletObservable(obs: Observable[Pagelet]) extends Appendable[PageletObservable] {
  def +=(other: PageletObservable): PageletObservable = PageletObservable(obs ++ other.obs)
}

object PageletObservable {
  val empty = PageletObservable(Observable.empty: Observable[Pagelet])
  def apply(text: String): PageletObservable = apply(Html(text))
  def apply(html: Html): PageletObservable = apply(Pagelet(html))
  def apply(pagelet: Pagelet): PageletObservable = apply(Observable.items(pagelet))
  def apply(elements: Seq[PageletObservable]): PageletObservable = elements.fold(empty)(_ += _)
}
