package com.evrone.http

import com.twitter.util.{Try,Return,Throw}
import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper}
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._


/*
class IntegrationSpec extends FunSpec {

  def withServer(test: integration.TestHttpServer => Any) {
    val server = new integration.TestHttpServer
    server.start()
    test(server)
    server.stop()
  }

  describe("make a json PUT request") {
    val client = new RestClient

    it("a successfuly") {
      val x = """{"a":1,"b":"2"}"""
      val mapper = new ObjectMapper

      withServer{ srv =>
        srv.handle("/echo") { case (req,res) =>
          (req.withMethod("PUT")
              .withParam("foo=bar2")
              .withHeader("Accept", "application/xml")
              .withBasicAuth("user", "pass"),
           res.withContentType("application/xml")
              .withCode(201)
              .withData(x))
        }

        expectResult(Return(mapper.readTree(x))) {
          client.put("http://" + srv.address + "/echo")
                .withParam("foo","bar")
                .withAccept("application/xml")
                .withBasicAuth("user", "pass").andThen.asJsonNode
        }
      }
    }
  }

  describe("make a xml GET request") {
    val client = new RestClient

    it("a successfuly") {
      val x = "<a>1</a>"

      withServer{ srv =>
        srv.handle("/echo") { case (req,res) =>
          (req.withMethod("GET")
              .withQueryString("foo=bar")
              .withHeader("Accept", "application/xml")
              .withBasicAuth("user", "pass"),
           res.withContentType("application/xml")
              .withCode(201)
              .withData(x))
        }

        expectResult(Return(xml.XML.loadString(x))) {
          client.get("http://" + srv.address + "/echo")
                .withQuery("foo", "bar")
                .withAccept("application/xml")
                .withBasicAuth("user", "pass").andThen.asXml
        }
      }
    }

    describe("a fail") {

      it ("empty response") {
        withServer{ srv =>
          srv.handle("/echo") { case (req,res) =>
            (req.withMethod("GET"),
             res.withCode(204)
                .withData(""))
          }

          val e = "Throw(com.evrone.http.restclient.response.HttpEntityDoesNotExists: HttpEntity does not exists)"
          client.get("http://" + srv.address + "/echo").andThen.asXml.toString should be (e)
        }
      }

      it ("invalid authentication") {
        withServer{ srv =>
          srv.handle("/echo") { case (req,res) =>
            (req.withMethod("GET")
                .withBasicAuth("u", "p"),
             res)
          }
          val e = "Throw(com.evrone.http.restclient.impl.UnexpectedResponse: HTTP/1.1 401 Unauthorized)"
          client.get("http://" + srv.address + "/echo").andThen.asXml.toString should be (e)
        }
      }

      it ("url not found") {
        withServer{ srv =>
          srv.handle("/echo") { case (req,res) =>
            (req.withMethod("GET"),
             res.withCode(204)
                .withData(""))
          }

          val e = "Throw(com.evrone.http.restclient.impl.UnexpectedResponse: HTTP/1.1 404 Not Found)"
          client.get("http://" + srv.address + "/not.found").andThen.asXml.toString should be (e)
        }
      }

      it ("invalid data") {
        withServer{ srv =>
          srv.handle("/echo") { case (req,res) =>
            (req.withMethod("GET"),
             res.withCode(201)
                .withData("<xml>1<xml>"))
          }

          val e = "Throw(org.xml.sax.SAXParseException"
          client.get("http://" + srv.address + "/echo").andThen.asXml.toString should startWith (e)
        }
      }
    }
  }
}

*/