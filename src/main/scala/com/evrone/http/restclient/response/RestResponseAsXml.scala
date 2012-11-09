package com.evrone.http.restclient.response

import com.twitter.util.Try
import org.apache.http.HttpResponse

object RestResponseAsXml extends RestResponse {
  def asXml(resp: HttpResponse): Try[xml.Elem] = {
    Try(xml.XML.loadString(getEntityString(resp)))
  }
}
