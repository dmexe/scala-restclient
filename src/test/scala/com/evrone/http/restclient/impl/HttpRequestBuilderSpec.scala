package com.evrone.http.restclient.impl

import org.apache.http.client.methods.{HttpGet,HttpHead,HttpPost,HttpRequestBase}
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import org.apache.http.HttpEntityEnclosingRequest
import org.apache.http.auth.AuthScope
import com.evrone.http.RestClient
import com.evrone.http.restclient.request.RestRequestBuilder

trait RestRequestBehaviors { this: FunSpec =>
  val client = new RestClient
}

/*
class HttpRequestBuilderSpec extends FunSpec with RestRequestBehaviors {

  val req = RestRequestBuilder(client, "GET", "http://example.com")

  describe(".prepare") {

    it("assign request headers") {
      val q = req.withHeader("name", "value")
      val header = HttpRequestBuilder(q).getFirstHeader("name")
      header.getValue() should be ("value")
    }

    it("assign basic auth") {
      val q = req.withBasicAuth("user", "pass")
      HttpRequestBuilder(q)
      val provider = req.client.httpClient.getCredentialsProvider()
      val cred = provider.getCredentials(AuthScope.ANY)
      cred.getUserPrincipal.getName should be ("user")
      info("user")
      cred.getPassword should be ("pass")
      info("password")
    }
  }

  describe(".GET") {
    val get = HttpRequestBuilder.GET(_)

    it("build a HttpGet request") {
      get(req).isInstanceOf[HttpGet] should be (true)
    }

    it should behave like queryString(get, req)
  }

  describe(".HEAD") {
    val head = HttpRequestBuilder.HEAD(_)

    it("build a HttpHead request") {
      head(req).isInstanceOf[HttpHead] should be (true)
    }

    it should behave like queryString(head, req)
  }

  describe(".POST") {
    val q = req.withData("data", "contentType")
    val post = HttpRequestBuilder.POST(_)

    it("build a HttpPost request") {
      post(q).isInstanceOf[HttpPost] should be (true)
    }

    it should behave like queryString(post, q)
    it should behave like postData(post, q)
    it should behave like formParams(post, q)
  }

  describe(".PUT") {
    val q = req.withData("data", "contentType").withMethod("PUT")
    val put = HttpRequestBuilder.POST(_)

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
*/
