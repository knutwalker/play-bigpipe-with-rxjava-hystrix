package controllers

import svc.ServiceClient
import play.api.mvc.{Cookie, Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._
import util.StaticContent

object ProfileViews extends Controller {

  def index(embed: Boolean) = Action.async {
    val profileViewsFuture = ServiceClient.profileViews()
    val kittensViewsFuture = ServiceClient.kittensViews()

    val template = if (embed) views.html.profileViews.body.apply _ else views.html.profileViews.profileViews.apply _

    val css = List(routes.Assets.at("stylesheets/pv.css").url)
    val js = List.empty[String]


    for {
      profileViews <- profileViewsFuture
      kittensViews <- kittensViewsFuture
    } yield {
      Ok(template(profileViews, kittensViews))
        .withCookies(Cookie("_pv", "foo"))
        .withHeaders(StaticContent.asHeaders(css, js): _*)
    }
  }
}
