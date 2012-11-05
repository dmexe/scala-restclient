package com.evrone.restclient

import org.apache.http.util.EntityUtils
import org.apache.http.{HttpEntity,HttpVersion,HttpStatus}
import org.apache.http.message.BasicHttpResponse
import org.apache.http.entity.{StringEntity,ContentType,ByteArrayEntity}
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

class ResponseSpec extends FunSpec {
  describe("as") {

    def withResponse(test: BasicHttpResponse => Any) {
      val response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK")
      test(response)
    }

    def responseWithStringEntity(resp: BasicHttpResponse, content:String) = {
      val entity = new StringEntity(content, "UTF-8")
      resp.setEntity(entity)
      resp
    }

    describe("a String") {

      describe("when success") {
        withResponse { resp =>
          val respWithEntity = responseWithStringEntity(resp, "response body")

          it("return Option[String] with body") {
            Response.StringResponse(respWithEntity) should be (Some("response body"))
          }
        }
      }

      describe("when fail") {
        withResponse { resp =>
          it("return None") {
            Response.StringResponse(resp) should be (None)
          }
        }
      }
    }

    describe("a xml.Elem") {

      describe("when success") {
        withResponse { resp =>
          val x =  "<a><b>1</b></a>"
          val respWithEntity = responseWithStringEntity(resp, x)

          it("return Option[xml.Elem] with body") {
            Response.XmlElemResponse(respWithEntity) should be (Some(xml.XML.loadString(x)))
          }
        }
      }

      describe("a fail") {
        withResponse { resp =>
          it("catch not xml") {
            val respWithEntity = responseWithStringEntity(resp, """{"a":"1"}""")
            Response.XmlElemResponse(resp) should be (None)
          }
          it("catch bad xml") {
            val respWithEntity = responseWithStringEntity(resp, "<a>1<a>")
            Response.XmlElemResponse(resp) should be (None)
          }
        }
      }
    }

    describe("a Array[Byte]") {

      describe("when success") {
        withResponse { resp =>
          val bytes = Array[Byte](1,2,3,4,5,6,7,8,9)
          val entity = new ByteArrayEntity(bytes)
          resp.setEntity(entity)

          it("return Option[Array[Byte]] with body") {
            val respBytes = Response.ByteArrayResponse(resp).get
            assert(java.util.Arrays.equals(respBytes, bytes), true)
          }
        }
      }

      describe("when fail") {
        withResponse { resp =>
          it("return None") {
            Response.ByteArrayResponse(resp) should be (None)
          }
        }
      }
    }
  }
}
