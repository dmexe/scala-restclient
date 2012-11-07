package com.evrone.http.restclient.response

import com.twitter.util.Try

trait RestResponseAsXml extends RestResponseBuilderBase {
  def asXml: Try[xml.Elem] = {
    asString.flatMap { body =>
      Try(xml.XML.loadString(body))
    }
  }
}
