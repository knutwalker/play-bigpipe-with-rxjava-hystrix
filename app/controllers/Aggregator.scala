package controllers

import play.api.mvc.{AnyContent, Request}


object Aggregator extends CommandStreamController {

  def pagelets(request: Request[AnyContent]) =
    ProfileUpdates.pagelets(request) ::: ProfileViews.pagelets(request) ::: NewKittens.pagelets(request)

  val layout = views.pagelet.aggregated.main
}
