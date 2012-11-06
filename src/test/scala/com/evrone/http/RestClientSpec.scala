package com.evrone.http

import com.evrone.http.restclient.impl.RestRequest
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

class RestClientSpec extends FunSpec {

  val client = new RestClient

  describe(".get") {
    it("build a new Request") {
      client.get("http://example.com").isInstanceOf[RestRequest] should be (true)
    }
  }

  describe(".post") {
    it("build a new Request") {
      client.post("http://example.com").isInstanceOf[RestRequest] should be (true)
    }
  }
}
