package com.evrone.restclient

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

class IntegrationSpec extends FunSpec {

  def withServer(test: integration.TestHttpServer => Any) {
    val server = new integration.TestHttpServer
    server.start()
    test(server)
    server.stop()
  }

  describe("make a PUT request") {
    val client = new RestClient

    it("a successfuly") {
      val x = "<a>1</a>"

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

        expectResult(Right(xml.XML.loadString(x))) {
          client.put("http://" + srv.address + "/echo")
                .withParam("foo","bar")
                .withBasicAuth("user", "pass").as[xml.Elem]
        }
      }
    }
  }

  describe("make a GET request") {
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

        expectResult(Right(xml.XML.loadString(x))) {
          client.get("http://" + srv.address + "/echo")
                .withQuery("foo", "bar")
                .withBasicAuth("user", "pass").as[xml.Elem]
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

          expectResult(Left("Fail to process response")) {
            client.get("http://" + srv.address + "/echo").as[xml.Elem]
          }
        }
      }

      it ("invalid authentication") {
        withServer{ srv =>
          srv.handle("/echo") { case (req,res) =>
            (req.withMethod("GET")
                .withBasicAuth("u", "p"),
             res)
          }

          expectResult(Left("HTTP/1.1 401 Unauthorized")) {
            client.get("http://" + srv.address + "/echo").as[xml.Elem]
          }
        }
      }

      it ("url not found") {
        withServer{ srv =>
          srv.handle("/echo") { case (req,res) =>
            (req.withMethod("GET"),
             res.withCode(204)
                .withData(""))
          }

          expectResult(Left("HTTP/1.1 404 Not Found")) {
            client.get("http://" + srv.address + "/not.found").as[xml.Elem]
          }
        }
      }
    }
  }
}
