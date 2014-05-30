package controllers

import svc.{ServiceClient2, ServiceClient}
import play.api.mvc.{Cookie, Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._
import util.RxPlay

object ProfileUpdates extends Controller {

  def index(embed: Boolean) = Action.async {
    val newCommentsFuture = ServiceClient.newComments()
    val newLikesFuture = ServiceClient.newLikes()

    val template = if (embed) views.html.profileUpdates.body.apply _ else views.html.profileUpdates.profileUpdates.apply _

    for {
      newComments <- newCommentsFuture
      newLikes <- newLikesFuture
    } yield {
      Ok(template(newComments, newLikes)).withCookies(Cookie("_pu", "bar"))
    }
  }
}
