package com.evrone.integration.restclient.example

import com.twitter.util.{Try,Return,Throw}
import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper}
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

import com.evrone.http.RestClient
import com.evrone.http.response.format._
import com.evrone.http.integration.TestHttpServer

trait IntegrationSpecHelper {this: FunSpec =>
  def withServer(f: TestHttpServer => Any) {
    val server = new TestHttpServer
    server.start()
    f(server)
    server.stop()
  }
}

class IntegrationSpec extends FunSpec with IntegrationSpecHelper {
  val client = new RestClient((v:String) => println(v))

  describe("make a json PUT request") {

    it("a successfuly") {
      val x = """{"a":1,"b":"2"}"""
      val mapper = new ObjectMapper

      withServer{ srv =>
        srv.handle("/echo") { (req,res) =>
          req.withMethod("PUT")
             .withParam("foo=bar2")
             .withHeader("Accept", "application/xml")
             .withBasicAuth("user", "pass") ->
          res.withContentType("application/xml")
             .withCode(201)
             .withData(x)}

        expectResult(Return(mapper.readTree(x))) {
          client.put("http://" + srv.address + "/echo")
                .withParam("foo","bar")
                .withAccept("application/xml")
                .withBasicAuth("user", "pass") andThen asJsonNode
        }
      }
    }
  }

  describe("make a xml GET request") {

    it("a successfuly") {
      val x = "<a>1</a>"

      withServer{ srv =>
        srv.handle("/echo") { (req,res) =>
          req.withMethod("GET")
             .withQueryString("foo=bar")
             .withHeader("Accept", "application/xml")
             .withBasicAuth("user", "pass") ->
          res.withContentType("application/xml")
             .withCode(201)
             .withData(x)}

        expectResult(Return(xml.XML.loadString(x))) {
          client.get("http://" + srv.address + "/echo")
                .withQuery("foo", "bar")
                .withAccept("application/xml")
                .withBasicAuth("user", "pass") andThen asXml
        }
      }
    }

    describe("a fail") {
      it ("empty response") {
        withServer{ srv =>
          srv.handle("/echo") { (req,res) =>
            req.withMethod("GET") ->
            res.withCode(204)
               .withData("")}

          val e = "Throw(org.xml.sax.SAXParseException:"
          (client.get("http://" + srv.address + "/echo") andThen asXml).toString should startWith (e)
        }
      }

      it ("wrong postData serialization") {
        val map = Map(1 -> "2")
        val e = "Throw(scala.MatchError:"
        (client.post("http://example.com").withJson(map) andThen(Return(_))).toString should startWith (e)
      }

      it ("invalid authentication") {
        withServer{ srv =>
          srv.handle("/echo") { (req,res) =>
            req.withMethod("GET")
               .withBasicAuth("u", "p") ->
            res }
          val e = "Throw(com.evrone.http.restclient.impl.UnexpectedHttpResponse: HTTP/1.1 401 Unauthorized)"
          (client.get("http://" + srv.address + "/echo") andThen asXml).toString should be (e)
        }
      }

      it ("url not found") {
        withServer{ srv =>
          srv.handle("/echo") { (req,res) =>
            req.withMethod("GET") ->
            res.withCode(204)
               .withData("")}

          val e = "Throw(com.evrone.http.restclient.impl.UnexpectedHttpResponse: HTTP/1.1 404 Not Found)"
          (client.get("http://" + srv.address + "/not.found") andThen asXml).toString should be (e)
        }
      }

      it ("invalid data") {
        withServer{ srv =>
          srv.handle("/echo") { (req,res) =>
            req.withMethod("GET") ->
            res.withCode(201)
               .withData("<xml>1<xml>")}

          val e = "Throw(org.xml.sax.SAXParseException"
          (client.get("http://" + srv.address + "/echo") andThen asXml).toString should startWith (e)
        }
      }
    }
  }
}
