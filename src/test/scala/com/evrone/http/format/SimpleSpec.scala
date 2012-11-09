package com.evrone.http.response.format

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import org.apache.http.HttpResponse
import org.apache.http.util.EntityUtils
import com.twitter.util.{Return,Try}

class SimpleSpec extends FunSpec with SpecHelper {

  describe("asString") {
    it("success") {
      val s = "string"
      asString(getStringResponse(s)) should be (Return(s))
    }
  }

  describe("asByteArray") {
    it("success") {
      val a = Array[Byte](1,2,3,4)
      asByteArray(getByteArrayResponse(a)).get.asInstanceOf[Array[Byte]] should be (a)
    }
  }
}
