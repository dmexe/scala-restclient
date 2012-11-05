package com.evrone.restclient

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

class RestClientSpec extends FunSpec {

  val client = new RestClient

  describe(".get") {
    it("build a new Request") {
      client.get("http://example.com").isInstanceOf[http.Request] should be (true)
    }
  }

  describe(".post") {
    it("build a new Request") {
      client.post("http://example.com").isInstanceOf[http.Request] should be (true)
    }
  }
}
