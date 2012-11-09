package com.evrone.http.response.format

import com.twitter.util.Try
import org.apache.http.HttpResponse

object asXml extends (HttpResponse => Try[xml.Elem]) with RestResponse {
  def apply(resp: HttpResponse): Try[xml.Elem] = {
    Try(xml.XML.loadString(getEntityString(resp)))
  }
}
