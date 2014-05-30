package controllers

import play.api.mvc.{Action, Controller}
import play.api.templates.{HtmlFormat, Html, Template1}
import svc.ServiceClient2
import util.{Pagelet, HtmlObservable}


object ProfileViewsEnum extends Controller {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  import util.ImplicitConversions.asEnumerator

  def index = Action {
    val profileViews = Pagelet.renderStream(ServiceClient2.profileViews(),
      views.html.profileViews.profileViewCount.apply).map(Pagelet.render(_, "profile-views"))
    val kittensViews = Pagelet.renderStream(ServiceClient2.kittensViews(),
      views.html.profileViews.kittensViewCount.apply).map(Pagelet.render(_, "kittens-views"))

    val htmlObservable = HtmlObservable(profileViews, kittensViews)
    val htmlStream = views.stream.profileViews.streamed(htmlObservable)

    Ok.chunked(asEnumerator(htmlStream))
  }
}
