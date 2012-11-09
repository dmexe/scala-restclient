package com.evrone.http.restclient.request

import com.evrone.http.RestClient
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

case class Address(street: String)
case class Phone(number:String, hours: Map[String,Int])
case class Person(id: Long, firstName:String, lastName:String, addresses: List[Address], phones: List[Phone])


class RestRequestBuilderSpec extends FunSpec {

  val restClient = new RestClient
  val restReqBuilder = RestRequestBuilder(restClient, "GET", "http://example.com")
  val map = Map("name" -> "value")

  describe(".withQuery") {
    it("from Map") {
      restReqBuilder.withQuery(map).query should be (map)
    }

    it("from String") {
      val q = "?query=1"
      restReqBuilder.withQuery(q).queryString.get should be (q)
    }

    it("from couple of String") {
      restReqBuilder.withQuery(map.head._1, map.head._2).query should be (map)
    }
  }

  describe(".withHeader") {
    it("from Map") {
      restReqBuilder.withHeader(map).headers should be (map)
    }

    it("from couple of String") {
      restReqBuilder.withHeader(map.head._1, map.head._2).headers should be (map)
    }
  }

  describe(".withParam") {
    it("from Map") {
      restReqBuilder.withParam(map).params should be (map)
    }

    it("from couple of String") {
      restReqBuilder.withParam(map.head._1, map.head._2).params should be (map)
    }
  }

  describe(".withBasicAuth") {
    it("from couple of String") {
      val (user, pass) = ("user", "pass")
      restReqBuilder.withBasicAuth(user, pass).basicAuth.get should be (user, pass)
    }
  }

  describe(".withData") {
    it("from couple of String") {
      val q = ("data", "content-type")
      restReqBuilder.withData(q._1, q._2).postDataAsString.get should be (q)
    }
  }

  describe("use same instance of client") {
    it("success") {
      val prevClient = restReqBuilder.client
      restReqBuilder.withQuery("q").client should be (prevClient)
    }
  }

  describe(".withJson") {
    it("from Map") {
      val data = Map("a" -> "b", "c" -> 2, "d" -> List(1,3,4,5))
      val expectedJson = """{"a":"b","c":2,"d":[1,3,4,5]}"""
      restReqBuilder.withJson(data).postDataAsString.get._1 should be (expectedJson)
      restReqBuilder.withJson(data).postDataAsString.get._2 should be ("application/json")
    }

    it("from case class") {
      val addr = Address("street")
      val phone = Phone("12718273", Map("2" -> 8))
      val person = Person(1, "first", "last", List(addr), List(phone))

      val json = """{"id":1,"firstName":"first","lastName":"last","addresses":[{"street":"street"}],"phones":[{"number":"12718273","hours":{"2":8}}]}"""
      restReqBuilder.withJson(person).postDataAsString.get._1 should be (json)
      restReqBuilder.withJson(person).postDataAsString.get._2 should be ("application/json")
    }

    it("when a fail") {
      val hash = Map(1 -> "2")
      val e = """Some(scala.MatchError:"""
      restReqBuilder.withJson(hash).postDataAsString should be (None)
      restReqBuilder.withJson(hash).hasError.toString should startWith(e)
    }
  }
}
