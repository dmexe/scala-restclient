package com.evrone.http.restclient.response

import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper}
import com.twitter.util.{Try,Return,Throw}
import org.apache.http.entity.{StringEntity,ContentType,ByteArrayEntity}
import org.apache.http.message.BasicHttpResponse
import org.apache.http.{HttpEntity,HttpVersion,HttpStatus}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import scala.io.Source

trait RestResponseSpecHelper { this: FunSpec =>
  def getHttpResponse = {
    new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK")
  }

  def getStringResponse(s: String) = {
    val entity = new StringEntity(s, "UTF-8")
    val httpResp = getHttpResponse
    httpResp.setEntity(entity)
    httpResp
  }

  def getByteArrayResponse(s: Array[Byte]) = {
    val entity = new ByteArrayEntity(s)
    val httpResp = getHttpResponse
    httpResp.setEntity(entity)
    httpResp
  }

  def resToStiring(s: String) = {
    Source.fromURL(getClass.getResource(s)).mkString
  }
}

class RestResponseBuilderSpec extends FunSpec with RestResponseSpecHelper {
/*
  describe("asString") {
    it("when success") {
      val s = "a test string"
      getStringResponse(s)
      withStringResponse(s) { resp =>
        RestResponseBuilder(resp).asString should be (Return(s))
      }
    }
    it("when fail") {
      val e = "Throw(com.evrone.http.restclient.response.HttpEntityDoesNotExists: HttpEntity does not exists)"
      withResponse{ resp =>
        RestResponseBuilder(resp).asString.toString should be (e)
      }
    }
  }

  describe("asByteArray") {
    it("when success") {
      val s = Array[Byte](1,2,3,4,5)
      withByteArrayResponse(s) { resp =>
        var call = false
        RestResponseBuilder(resp).asByteArray.map {v =>
          call = true
          v should be (s)
        }
        call should be (true)
      }
    }
    it("when fail") {
      val e = "Throw(com.evrone.http.restclient.response.HttpEntityDoesNotExists: HttpEntity does not exists)"
      withResponse{ resp =>
        RestResponseBuilder(resp).asByteArray.toString should be (e)
      }
    }
  }

  describe("asXml") {
    it("when success") {
      val x = "<xml>1</xml>"
      val r =  Return(xml.XML.loadString(x))
      withStringResponse(x) { resp =>
        RestResponseBuilder(resp).asXml should be (r)
      }
    }
    it("when fail") {
      val x = "<xml>1"
      val e = "Throw(org.xml.sax.SAXParseException"
      withStringResponse(x) { resp =>
        RestResponseBuilder(resp).asXml.toString should startWith (e)
      }
    }
  }

  describe("asJsonNode") {
    val mapper = new ObjectMapper

    it("when success") {
      val j = """{"a":"1","b":2}"""
      val r =  Return(mapper.readTree(j))
      withStringResponse(j) { resp =>
        RestResponseBuilder(resp).asJsonNode should be (r)
      }
    }
    it("when fail") {
      val x = "<xml>1"
      val e = "Throw(com.fasterxml.jackson.core.JsonParseException"
      withStringResponse(x) { resp =>
        RestResponseBuilder(resp).asJsonNode.toString should startWith (e)
      }
    }
  }

  describe("asJson") {
    it("when success") {
      val j = """{"a":"1","b":2}"""
      val r = "Return(JObject(List((a,JString(1)), (b,JInt(2)))))"
      withStringResponse(j) { resp =>
        RestResponseBuilder(resp).asJson.toString should be (r)
      }
    }
    it("when fail") {
      val x = "<xml>1"
      val e = "Throw(com.fasterxml.jackson.core.JsonParseException"
      withStringResponse(x) { resp =>
        RestResponseBuilder(resp).asJson.toString should startWith (e)
      }
    }
  }

  describe("asJson[T]") {
    case class Address(street:String, city:String)
    case class PersonWithAddresses(name: String, addresses: Map[String, Address])

    case class GlossDef()
    case class GlossEntry(ID:String,SortAs:String,GlossTerm:String,
                          Acronym:String,Abbrev:String,GlossDef:GlossDef)
    case class GlossList(GlossEntry:GlossEntry)
    case class GlossDiv(title:String,GlossList:GlossList)
    case class Glossary(title:String,GlossDiv:GlossDiv)
    case class Document(glossary:Glossary)

    it("when success (a basic example)") {
      val s = resourceAsString("/example.json")
      s.length should be (244)
      val rs = "Return(PersonWithAddresses(joe,Map(address1 -> Address(Bulevard,Helsinki), address2 -> Address(Soho,London))))"
      withStringResponse(s) { resp =>
        RestResponseBuilder(resp).asJson[PersonWithAddresses].toString should be (rs)
      }
    }

    it("when success (a http://www.json.org/example.html)") {
      val s = resourceAsString("/example2.json")
      s.length should be (566)
      val rs = "Return(Document(Glossary(example glossary,GlossDiv(S,GlossList(GlossEntry(SGML,SGML,Standard Generalized Markup Language,SGML,ISO 8879:1986,GlossDef()))))))"
      withStringResponse(s) { resp =>
        RestResponseBuilder(resp).asJson[Document].toString should be (rs)
      }
    }

    it("when fail") {
      val s = resourceAsString("/example2.json")
      s.length should be (566)
      val rs = "Throw(org.json4s.MappingException"
      withStringResponse(s) { resp =>
        RestResponseBuilder(resp).asJson[PersonWithAddresses].toString should startWith (rs)
      }
    }
  }
*/
}
