package com.evrone.http.restclient.response

import org.json4s._
import org.json4s.jackson.JsonMethods._

trait RestResponseAs extends RestResponseBuilder {
  def as[T](implicit mf: scala.reflect.Manifest[T]): Option[T] = {
    asString.flatMap { body =>
      Some(parse(body).extract[T](DefaultFormats, mf))
    }
  }
}
