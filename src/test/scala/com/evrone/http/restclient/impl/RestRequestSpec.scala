package com.evrone.http.restclient.impl

import com.evrone.http.RestClient
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

case class Address(street: String)
case class Phone(number:String, hours: Map[String,Int])
case class Person(id: Long, firstName:String, lastName:String, addresses: List[Address], phones: List[Phone])

/*
class RestRequestSpec extends FunSpec {

  val log = (s:String) => println(s)
  val client = new RestClient(log)
  val req = RestRequestBuilder(client, "GET", "http://example.com")
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
    req.withData(q._1, q._2).postDataAsString.get should be (q)
  }

  it("use same instance of client") {
    val prevClient = req.client
    req.withQuery("q").client should be === (prevClient)
  }

  it("serialize object to json") {
    val data = Map("a" -> "b", "c" -> 2, "d" -> List(1,3,4,5))
    val json = """{"a":"b","c":2,"d":[1,3,4,5]}"""
    req.withJson(data).postDataAsString.get._1 should be (json)
    req.withJson(data).postDataAsString.get._2 should be ("application/json")
  }

  it("serialize object to json (case class)") {
    val addr = Address("street")
    val phone = Phone("12718273", Map("2" -> 8))
    val person = Person(1, "first", "last", List(addr), List(phone))

    val json = """{"id":1,"firstName":"first","lastName":"last","addresses":[{"street":"street"}],"phones":[{"number":"12718273","hours":{"2":8}}]}"""
    req.withJson(person).postDataAsString.get._1 should be (json)
    req.withJson(person).postDataAsString.get._2 should be ("application/json")
  }

  it("fail to searialize object to json") {
    val hash = Map(1 -> "2")
    val e = """RestResponseBuilder(Throw(scala.MatchError"""
    req.withJson(hash).postDataAsString should be (None)
    req.withJson(hash).andThen.toString should startWith (e)
  }
}
*/
