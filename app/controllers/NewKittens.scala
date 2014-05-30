package controllers

import play.api.mvc.{Action, Controller}
import svc.ServiceClient
import play.api.libs.concurrent.Execution.Implicits._

object NewKittens extends Controller {

  def index(embed: Boolean) = Action.async {
    val newKittensFuture = ServiceClient.newKittens()

    for {
      newKitten <- newKittensFuture
    } yield {
      if (embed) Ok(views.html.newKittens_so.body(newKitten))
      else Ok(views.html.newKittens_so.scaffold(newKitten))
    }
  }

}
