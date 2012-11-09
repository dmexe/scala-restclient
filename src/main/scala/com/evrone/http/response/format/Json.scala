package com.evrone.http.response

import com.twitter.util.Try
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.apache.http.HttpResponse

package object format {
  object asJValue extends (HttpResponse => Try[JValue]) with RestResponse {
    def apply(resp: HttpResponse): Try[JValue] = {
      Try(parse(getEntityString(resp)))
    }
  }

  def asJson[T](resp: HttpResponse)(implicit formats: Formats = DefaultFormats,
                                             mf: scala.reflect.Manifest[T]): Try[T] = {
    Try(parse(asJValue.getEntityString(resp)).extract[T](formats, mf))
  }
}
