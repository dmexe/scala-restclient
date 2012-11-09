package com.evrone.http.response.format

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import org.json4s._
import org.json4s.jackson.JsonMethods._

class JsonSpec extends FunSpec with SpecHelper {
  describe("asJValue") {
    it("success") {
      val s = """{"a":1,"b":"2"}"""
      asJValue(getStringResponse(s)).get should be (parse(s))
    }
    it("fail") {
      val s = """{"a:1,"b":"2"}"""
      val e = "Throw(com.fasterxml.jackson.core.JsonParseException"
      asJValue(getStringResponse(s)).toString should startWith (e)
    }
  }

  describe("asJson") {
    case class Address(street:String, city:String)
    case class PersonWithAddresses(name: String, addresses: Map[String, Address])

    case class GlossDef()
    case class GlossEntry(ID:String,SortAs:String,GlossTerm:String,
                          Acronym:String,Abbrev:String,GlossDef:GlossDef)
    case class GlossList(GlossEntry:GlossEntry)
    case class GlossDiv(title:String,GlossList:GlossList)
    case class Glossary(title:String,GlossDiv:GlossDiv)
    case class Document(glossary:Glossary)

    it("success (a basic example)") {
      val s = resourceToString("/example.json")
      s.length should be (244)
      val e = PersonWithAddresses("joe",
                                   Map("address1" -> Address("Bulevard", "Helsinki"),
                                       "address2" -> Address("Soho", "London")))
      asJson[PersonWithAddresses](getStringResponse(s)).get should be (e)
    }

    it("success (a http://www.json.org/example.html)") {
      val s = resourceToString("/example2.json")
      s.length should be (566)
      val e = "Document(Glossary(example glossary,GlossDiv(S,GlossList(GlossEntry(SGML,SGML,Standard Generalized Markup Language,SGML,ISO 8879:1986,GlossDef())))))"
      asJson[Document](getStringResponse(s)).get.toString should be (e)
    }

    it("fail") {
      val s = resourceToString("/example2.json")
      s.length should be (566)
      val e = "Throw(org.json4s.MappingException"
      asJson[PersonWithAddresses](getStringResponse(s)).toString should startWith (e)

    }
  }
}
