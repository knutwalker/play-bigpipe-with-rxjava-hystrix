package controllers

import play.api.mvc.{AnyContent, Request}
import commands._
import views.html.newKittens._

object NewKittens extends CommandStreamController {

  def pagelets(request: Request[AnyContent]) = {
    val newestKitten = command(NewestKittenCommand(), sayHello.apply, "newest-kitten")
    List(newestKitten)
  }

  val layout = views.pagelet.newKittens.streamed
}
