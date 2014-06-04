package controllers

import play.api.mvc.{AnyContent, Request}
import commands._
import views.html.profileUpdates._

object ProfileUpdates extends CommandStreamController {

  def pagelets(request: Request[AnyContent]) = {
    val moduleCss = resources(css = List("styles/profile-updates.css"))
    val newComments = command(NewCommentsCommand(), newCommentsCount.apply, "new-comments")
    val newLikes = command(NewLikesCommand(), newLikesCount.apply, "new-likes")
    List(moduleCss, newComments, newLikes)
  }

  val layout = views.pagelet.profileUpdates.streamed
}
