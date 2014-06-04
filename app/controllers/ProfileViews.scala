package controllers

import play.api.mvc.{AnyContent, Request}
import commands._
import views.html.profileViews._

object ProfileViews extends CommandStreamController {

  def pagelets(request: Request[AnyContent]) = {
    val moduleCss = resources(css = List("styles/profile-views.css"))
    val profileViews = command(ProfileViewsCommand(), profileViewCount.apply, "profile-views")
    val kittensViews = command(KittensViewsCommand(), kittensViewCount.apply, "kittens-views")
    List(moduleCss, profileViews, kittensViews)
  }

  val layout = views.pagelet.profileViews.streamed
}
