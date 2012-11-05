package com.evrone.restclient

import org.scalatest._

class RestClientSpec extends WordSpec {

  val client = new RestClient

  ".get" should {
    "build a new Request" in {
      assert(client.get("http://example.com").isInstanceOf[http.Request], true)
    }
  }
}
