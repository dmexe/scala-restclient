package com.evrone.restclient.response

trait RestResponseAsXml extends RestResponseBuilder {
  def asXml: Option[xml.Elem] = {
    asString.flatMap { body =>
      try {
        Some(xml.XML.loadString(body))
      } catch {
        case e:org.xml.sax.SAXParseException => None
      }
    }
  }
}
