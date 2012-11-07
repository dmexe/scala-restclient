package com.evrone.http.restclient.response

import com.twitter.util.Try
import org.json4s._
import org.json4s.jackson.JsonMethods._

trait RestResponseAsJson extends RestResponseBuilderBase {
  def asJson: Try[JValue] = {
    asString.flatMap { body =>
      Try(parse(body))
    }
  }

  def asJson[T](implicit formats:Formats = DefaultFormats, mf: scala.reflect.Manifest[T]): Try[T] = {
    asString.flatMap { body =>
      Try(parse(body).extract[T](formats, mf))
    }
  }
}

/*
case e:JsonMappingException => None
case e:JsonParseException => None
*/
