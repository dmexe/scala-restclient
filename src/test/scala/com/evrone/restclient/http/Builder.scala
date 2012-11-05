package com.evrone.restclient.http

import org.apache.http.client.methods.{HttpGet,HttpHead,HttpPost,HttpRequestBase}
import org.apache.http.HttpEntityEnclosingRequest
import org.apache.http.util.EntityUtils
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import org.apache.http.auth.AuthScope

trait RequestBehaviors { this: FunSpec =>
  val client = new com.evrone.restclient.RestClient()

  def queryString(http: Request => HttpRequestBase, req: Request) {
    it("assign queryString") {
      val q = req.withQuery("a=1")
      http(q).getURI.toASCIIString should be ("http://example.com?a=1")
      http(q.withQuery("b", "2")).getURI.toASCIIString should be ("http://example.com?a=1&b=2")
      info("and queryParams")
    }
  }

  def postData(http: Request =>  HttpEntityEnclosingRequest, req: Request) {
    it("assign postData") {
      val e = http(req).getEntity
      EntityUtils.toString(e) should be ("data")
      EntityUtils.consume(e)

      e.getContentType().getValue() should be ("contentType")
      info("and postData content type")
    }
  }

  def formParams(http: Request =>  HttpEntityEnclosingRequest, req: Request) {
    it("assign form params") {
      val q = req.withParam("name", "value")
      val e = http(q).getEntity

      EntityUtils.toString(e) should be ("name=value")
      EntityUtils.consume(e)

      e.getContentType().getValue() should be ("application/x-www-form-urlencoded; charset=UTF-8")
      info("and params content type")
    }
  }
}

class BuilderSpec extends FunSpec with RequestBehaviors {

  val req = Request(client, "GET", "http://example.com")

  describe(".prepare") {

    it("assign request headers") {
      val q = req.withHeader("name", "value")
      val header = Builder(q).getFirstHeader("name")
      header.getValue() should be ("value")
    }

    it("assign basic auth") {
      val q = req.withBasicAuth("user", "pass")
      Builder(q)
      val provider = req.client.httpClient.getCredentialsProvider()
      val cred = provider.getCredentials(AuthScope.ANY)
      cred.getUserPrincipal.getName should be ("user")
      info("user")
      cred.getPassword should be ("pass")
      info("password")
    }
  }

  describe(".GET") {
    val get = Builder.GET(_)

    it("build a HttpGet request") {
      get(req).isInstanceOf[HttpGet] should be (true)
    }

    it should behave like queryString(get, req)
  }

  describe(".HEAD") {
    val head = Builder.HEAD(_)

    it("build a HttpHead request") {
      head(req).isInstanceOf[HttpHead] should be (true)
    }

    it should behave like queryString(head, req)
  }

  describe(".POST") {
    val q = req.withData("data", "contentType")
    val post = Builder.POST(_)

    it("build a HttpPost request") {
      post(q).isInstanceOf[HttpPost] should be (true)
    }

    it should behave like queryString(post, q)
    it should behave like postData(post, q)
    it should behave like formParams(post, q)
  }

  describe(".PUT") {
    val q = req.withData("data", "contentType").withMethod("PUT")
    val put = Builder.POST(_)

    it("build a HttpPost request") {
      put(q).isInstanceOf[HttpPost] should be (true)
    }

    it("getMethod() should be PUT") {
      put(q).getMethod() should be ("PUT")
    }

    it should behave like queryString(put, q)
    it should behave like postData(put, q)
    it should behave like formParams(put, q)
  }
}
