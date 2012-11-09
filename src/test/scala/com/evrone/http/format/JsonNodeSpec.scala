package com.evrone.http.response.format

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import com.twitter.util.{Return,Try}
import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper}

class JsonNodeSpec extends FunSpec with SpecHelper {
  val mapper = new ObjectMapper

  describe("asJsonNode") {
    it("success") {
      val s = """{"a":1,"b":"2"}"""
      asJsonNode(getStringResponse(s)).get should be (mapper.readTree(s))
    }
    it("fail") {
      val s = """{"a:1,"b":"2"}"""
      val e = "Throw(com.fasterxml.jackson.core.JsonParseException"
      asJsonNode(getStringResponse(s)).toString should startWith (e)
    }
  }
}
