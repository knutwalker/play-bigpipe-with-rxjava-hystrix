package controllers

import com.netflix.hystrix.HystrixCommand
import pagelet.{CommandPagelet, Pagelet, PageletFactory, PageletObservable, ResourcePagelet}
import play.api.mvc.{Action, AnyContent, Controller, Request}
import play.twirl.api.{Html, Template1}
import rx.lang.scala.Observable


/**
 * Helper for controller that are building command streams to be uses for BigPipe
 */
trait CommandStreamController extends Controller {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  /** creates a pagelet factory based on a Hystrix command and a view. */
  protected def command[T](cmd: HystrixCommand[T], view: T => Html, id: String): PageletFactory = CommandPagelet(cmd, view, id)

  /** declares a resource, that should be used by the module */
  protected def resources(js: List[String] = Nil, css: List[String] = Nil): PageletFactory = ResourcePagelet(js, css)

  /** what pagelets a module consists of. */
  def pagelets(request: Request[AnyContent]): Seq[PageletFactory]

  /** the layout to use, when this module is rendered standalone */
  def layout: Template1[PageletObservable, PageletObservable]

  /** renders the standalone module using the BigPie technique */
  def index = Action { request =>
    val observables = pagelets(request).map(_.apply())
    val enumerated = Pagelet.enumerate(observables: _*)(layout.render)
    Ok.chunked(enumerated)
  }

  /** renders the standalone module, but blocks before sending data as if it were a traditional serving model */
  def blocking = Action { request =>
    val snippets = pagelets(request).map(_.blocking())
    val observable = Observable.items(snippets: _*)
    val enumerated = Pagelet.enumerate(observable)(layout.render)
    Ok.chunked(enumerated)
  }
}
