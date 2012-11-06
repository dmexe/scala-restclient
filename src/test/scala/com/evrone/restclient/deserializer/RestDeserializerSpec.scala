package com.evrone.restclient.deserializer

import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper}
import org.apache.http.entity.{StringEntity,ContentType,ByteArrayEntity}
import org.apache.http.message.BasicHttpResponse
import org.apache.http.util.EntityUtils
import org.apache.http.{HttpEntity,HttpVersion,HttpStatus}
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import org.json4s._
import org.json4s.jackson.JsonMethods._

class RestDeserializerSpec extends FunSpec {
  import RestDeserializer._

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

          it("return Option[String]") {
            ToStringDeserializer(respWithEntity) should be (Some("response body"))
          }
        }
      }

      describe("when fail") {
        withResponse { resp =>
          it("return None") {
            ToStringDeserializer(resp) should be (None)
          }
        }
      }
    }

    describe("a xml.Elem") {

      describe("when success") {
        withResponse { resp =>
          val x =  "<a><b>1</b></a>"
          val respWithEntity = responseWithStringEntity(resp, x)
          it("return Option[xml.Elem]") {
            ToXmlElemDeserializer(respWithEntity) should be (Some(xml.XML.loadString(x)))
          }
        }
      }

      describe("when fail") {
        withResponse { resp =>
          it("catch not xml") {
            val respWithEntity = responseWithStringEntity(resp, """{"a":"1"}""")
            ToXmlElemDeserializer(resp) should be (None)
          }
          it("catch bad xml") {
            val respWithEntity = responseWithStringEntity(resp, "<a>1<a>")
            ToXmlElemDeserializer(resp) should be (None)
          }
        }
      }
    }

    describe("a JsonNode") {

      describe("when success") {
        withResponse { resp =>
          val x =  """{"a":1,"b":"2"}"""
          val respWithEntity = responseWithStringEntity(resp, x)
          val mapper = new ObjectMapper
          val json = mapper.readTree(x)
          it("return Option[JsonNode]") {
            ToJsonNodeDeserializer(respWithEntity).get should be (json)
          }
        }

        describe("when fail") {
          withResponse { resp =>
            it("catch not json") {
              val respWithEntity = responseWithStringEntity(resp, """<a>2</a>""")
              ToJsonNodeDeserializer(resp) should be (None)
            }
            it("catch bad json") {
              val respWithEntity = responseWithStringEntity(resp, """{"a":2,"b":"1" """)
              ToJsonNodeDeserializer(resp) should be (None)
            }
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
          it("return Option[Array[Byte]]") {
            val respBytes = ToByteArrayDeserializer(resp).get
            assert(java.util.Arrays.equals(respBytes, bytes), true)
          }
        }
      }

      describe("when fail") {
        withResponse { resp =>
          it("return None") {
            ToByteArrayDeserializer(resp) should be (None)
          }
        }
      }
    }
  }
}
