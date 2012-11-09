package com.evrone.http.response.format

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import org.apache.http.HttpResponse
import org.apache.http.util.EntityUtils
import com.twitter.util.{Return,Try}

class XmlSpec extends FunSpec with SpecHelper {

  describe("asXml") {
    it("success") {
      val s = "<xml>1</xml>"
      asXml(getStringResponse(s)).get should be (xml.XML.loadString(s))
    }
    it("fail") {
      val s = "<xml>1</xml"
      val e = "Throw(org.xml.sax.SAXParseException"
      asXml(getStringResponse(s)).toString should startWith (e)
    }
  }
}
