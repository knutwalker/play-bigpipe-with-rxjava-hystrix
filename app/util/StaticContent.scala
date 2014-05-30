package util

import play.api.mvc.SimpleResult

import scala.collection.breakOut
import scala.collection.immutable.Seq

object StaticContent {

  val cssHeaderName = "X-CSS"
  val jsHeaderName = "X-JS"

  def asHeaders(css: Seq[String], js: Seq[String]): Seq[(String, String)] =
    Some(css).filter(_.nonEmpty).map(cssHeaderName -> _.mkString(",")).toList :::
    Some(js).filter(_.nonEmpty).map(jsHeaderName -> _.mkString(",")).toList

  def mergeCssHeaders(results: SimpleResult*): Seq[String] = mergeHeaderValues(cssHeaderName, results: _*)
  def mergeJsHeaders(results: SimpleResult*): Seq[String] = mergeHeaderValues(jsHeaderName, results: _*)

  private def mergeHeaderValues(headerName: String, results: SimpleResult*): Seq[String] =
    mergeHeaderValues(headerName, _.split(',').toList, results: _*).distinct

  private[util] def mergeHeaderValues[A](headerName: String, decoder: String => Seq[A], results: SimpleResult*): Seq[A] =
    results.flatMap(_.header.headers.get(headerName).map(decoder).getOrElse(Seq()))(breakOut)
}
