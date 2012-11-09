package com.evrone.http.restclient.impl

import org.apache.http.client.methods.{HttpGet,HttpHead,HttpPost,HttpRequestBase}
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import org.apache.http.HttpEntityEnclosingRequest
import org.apache.http.auth.AuthScope
import com.evrone.http.RestClient
import com.evrone.http.restclient.request.RestRequestBuilder
import org.apache.http.util.EntityUtils

trait RestRequestBehaviors { this: FunSpec =>
  val client = new RestClient
  val restReqBuilder = RestRequestBuilder(client, "GET", "http://example.com")

  def queryString(method: String) {
    it("assign queryString") {
      val q = restReqBuilder.withQuery("a=1").withMethod(method)
      HttpRequestBuilder(q).getURI.toASCIIString should be ("http://example.com?a=1")
      HttpRequestBuilder(q.withQuery("b", "2")).getURI.toASCIIString should be ("http://example.com?a=1&b=2")
      info("and queryParams")
    }
  }

  def postData(method: String) {
    it("assign postDataAsString") {
      val q = restReqBuilder.withData("data", "contentType").withMethod(method)
      val e = HttpRequestBuilder(q).asInstanceOf[HttpEntityEnclosingRequest].getEntity
      EntityUtils.toString(e) should be ("data")
      EntityUtils.consume(e)

      e.getContentType.getValue should be ("contentType")
      info("and postDataAsString content type")
    }
  }

  def formParams(method: String) {
    it("assign form params") {
      val q = restReqBuilder.withParam("name", "value").withMethod(method)
      val e = HttpRequestBuilder(q).asInstanceOf[HttpEntityEnclosingRequest].getEntity

      EntityUtils.toString(e) should be ("name=value")
      EntityUtils.consume(e)

      e.getContentType.getValue should be ("application/x-www-form-urlencoded; charset=UTF-8")
      info("and params content type")
    }
  }
}

class HttpRequestBuilderSpec extends FunSpec with RestRequestBehaviors {

  describe("Assigns") {

    it("request headers") {
      val req = restReqBuilder.withHeader("name", "value")
      HttpRequestBuilder(req).getFirstHeader("name").getValue should be ("value")
    }

    it("basic auth") {
      val req = restReqBuilder.withBasicAuth("user", "pass")
      HttpRequestBuilder(req)
      val provider = req.client.httpClient.getCredentialsProvider()
      val cred = provider.getCredentials(AuthScope.ANY)
      cred.getUserPrincipal.getName should be ("user")
      info("user")
      cred.getPassword should be ("pass")
      info("password")
    }
  }

  describe("GET request") {
    val get = HttpRequestBuilder(restReqBuilder.withMethod("GET"))

    it("build a HttpGet instance") {
      get.isInstanceOf[HttpGet] should be (true)
    }

    it should behave like queryString("GET")
  }

  describe("HEAD request") {
    val head = HttpRequestBuilder(restReqBuilder.withMethod("HEAD"))

    it("build a HttpHead instance") {
      head.isInstanceOf[HttpHead] should be (true)
    }

    it should behave like queryString("HEAD")
  }

  describe("POST request") {
    val req = restReqBuilder.withData("data", "contentType").withMethod("POST")
    val post = HttpRequestBuilder(req)

    it("build a HttpPost instance") {
      post.isInstanceOf[HttpPost] should be (true)
    }

    it should behave like queryString("POST")
    it should behave like postData("POST")
    it should behave like formParams("POST")
  }

  describe("PUT request") {
    val req = restReqBuilder.withData("data", "contentType").withMethod("PUT")
    val put = HttpRequestBuilder(req)

    it("build a HttpPost instance") {
      put.isInstanceOf[HttpPost] should be (true)
    }

    it("getMethod() of HttpPost request should be PUT") {
      put.getMethod should be ("PUT")
    }

    it should behave like queryString("POST")
    it should behave like postData("POST")
    it should behave like formParams("POST")
  }
}
