package svc

import commands._
import rx.lang.scala.Observable

import scala.concurrent.Future

object ServiceClient {
  import util.Futures._

  def newKittens(): Future[String] = NewestKitten().future
  def profileViews(): Future[Int] = ProfileViews().future
  def kittensViews(): Future[Int] = KittensViews().future
  def newComments(): Future[Int] = NewComments().future
  def newLikes(): Future[Int] = NewLikes().future
}

object ServiceClient2 {
  import rx.lang.scala.JavaConversions._

  def newKittens(): Observable[String] = NewestKitten().observe()
  def profileViews(): Observable[Int] = ProfileViews().observe()
  def kittensViews(): Observable[Int] = KittensViews().observe()
  def newComments(): Observable[Int] = NewComments().observe()
  def newLikes(): Observable[Int] = NewLikes().observe()
}
