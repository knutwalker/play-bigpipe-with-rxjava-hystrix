package pagelet

import com.netflix.hystrix.HystrixCommand
import controllers.routes
import play.twirl.api.Html
import rx.lang.scala.JavaConversions.toScalaObservable
import rx.lang.scala.Observable


/**
 * A container for creating Pagelets in a non-blocking and blocking manner.
 */
sealed abstract class PageletFactory extends (() => Observable[Pagelet]) {

  def blocking(): Pagelet
}

/**
 * A Pagelet based on a HystrixCommand
 * @param command the Hystrix powering the content of this pagelet
 * @param view  a template function, that renders the html for this pagelet
 * @param id    the ID of the placeholder element in the layout
 * @tparam T    the type of the content
 */
case class CommandPagelet[T](command: HystrixCommand[T], view: T => Html, id: String) extends PageletFactory {

  private def toPagelet(content: T) = Pagelet.render(view(content), id, Nil, Nil)

  def apply(): Observable[Pagelet] = {
    val observable: Observable[T] = command.observe()
    observable.map(toPagelet)
  }

  def blocking(): Pagelet = toPagelet(command.execute())
}

/**
 * A Pagelet containing only resources
 * @param js  a list to paths of javascript resources
 * @param css a list of paths to css resources
 */
case class ResourcePagelet(js: List[String] = Nil, css: List[String] = Nil) extends PageletFactory {

  def apply(): Observable[Pagelet] = {
    Observable.just(blocking())
  }

  def blocking(): Pagelet = {
    val fakeId = getClass.getSimpleName
    val jsUrls = js.map(j => routes.Assets.at(j).url)
    val cssUrls = css.map(c => routes.Assets.at(c).url)
    Pagelet.render(Html(""), fakeId, jsUrls, cssUrls)
  }
}
