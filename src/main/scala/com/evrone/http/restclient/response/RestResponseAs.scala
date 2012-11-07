package com.evrone.http.restclient.response

import com.twitter.util.Try
import org.json4s._
import org.json4s.jackson.JsonMethods._

trait RestResponseAs extends RestResponseBuilder {
  def as[T](implicit mf: scala.reflect.Manifest[T]): Try[T] = {
    asString.flatMap { body =>
      Try(parse(body).extract[T](DefaultFormats, mf))
    }
  }
}
