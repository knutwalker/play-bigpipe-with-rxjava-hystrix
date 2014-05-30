package controllers

import play.api.mvc._
import svc.ServiceClient
import play.api.libs.concurrent.Execution.Implicits._

object Application extends Controller {

  def index = Action(Ok("hello"))

//  def index = Action.async {
//    val newKittensFuture = ServiceClient.newKittens()
//    val profileViewsFuture = ServiceClient.profileViews()
//
//    for {
//      profileViews <- profileViewsFuture
//      newKittens <- newKittensFuture
//    } yield {
//      Ok(views.html.index(newKittens, profileViews))
//    }
//  }

}
