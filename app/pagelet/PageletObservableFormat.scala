package pagelet

import play.twirl.api.{Format, HtmlFormat}
import scala.collection.immutable.Seq

/**
 * The custom format specification for streamable templates
 */
object PageletObservableFormat extends Format[PageletObservable] {

  def raw(text: String): PageletObservable = PageletObservable(text)

  def escape(text: String): PageletObservable = PageletObservable(HtmlFormat.escape(text))

  def empty: PageletObservable = PageletObservable.empty

  def fill(elements: Seq[PageletObservable]): PageletObservable = PageletObservable(elements)
}
