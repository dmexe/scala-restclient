package com.evrone.http.restclient.response

import com.twitter.util.Try
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.apache.http.HttpResponse

object RestResponseAsJson extends RestResponse {
  def asJson(resp: HttpResponse): Try[JValue] = {
    Try(parse(getEntityString(resp)))
  }

  def asJson[T](resp: HttpResponse)(implicit formats: Formats = DefaultFormats,
                                             mf: scala.reflect.Manifest[T]): Try[T] = {
    Try(parse(getEntityString(resp)).extract[T](formats, mf))
  }
}