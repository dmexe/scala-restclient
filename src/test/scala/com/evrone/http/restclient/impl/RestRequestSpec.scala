package com.evrone.http.restclient.impl

import com.evrone.http.RestClient
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

class RestRequestSpec extends FunSpec {

  val client = new RestClient()
  val req = RestRequest(client, "GET", "http://example.com")
  val map = Map("name" -> "value")

  it("add a query from Map") {
    req.withQuery(map).query should be (map)
  }

  it("add a query from string") {
    val q = "?query=1"
    req.withQuery(q).queryString.get should be (q)
  }

  it("add query from couple of string") {
    req.withQuery(map.head._1, map.head._2).query should be (map)
  }

  it("add headers from Map") {
    req.withHeader(map).headers should be (map)
  }

  it("add headers from couple of string") {
    req.withHeader(map.head._1, map.head._2).headers should be (map)
  }

  it("add params from Map") {
    req.withParam(map).params should be (map)
  }

  it("add params from couple of string") {
    req.withParam(map.head._1, map.head._2).params should be (map)
  }

  it("setup BasicAuth") {
    val (user, pass) = ("user", "pass")
    req.withBasicAuth(user, pass).basicAuth.get should be (user, pass)
  }

  it("add a raw post data") {
    val q = ("data", "content-type")
    req.withData(q._1, q._2).postData.get should be (q)
  }

  it("use same instance of client") {
    val prevClient = req.client
    req.withQuery("q").client should be === (prevClient)
  }
}
