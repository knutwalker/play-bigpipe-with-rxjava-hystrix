package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._
import util.Pagelet


object Aggregator extends Controller {

  def index = Action.async { request =>
    val profileUpdatesFuture = ProfileUpdates.index(embed = true)(request)
    val profileViewsFuture = ProfileViews.index(embed = true)(request)

    for {
      profileUpdates <- profileUpdatesFuture
      profileViews <- profileViewsFuture

      profileUpdatesBody <- Pagelet.readBody(profileUpdates)
      profileViewsBody <- Pagelet.readBody(profileViews)
    } yield {
      Ok(views.html.aggregator.aggregator(profileUpdatesBody, profileViewsBody)).withCookies(Pagelet.mergeCookies(profileUpdates, profileViews): _*)
    }
  }

}
