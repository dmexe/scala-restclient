package com.evrone.restclient.deserializer

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper,JsonMappingException}
import org.apache.http.util.EntityUtils
import org.apache.http.{HttpEntity,HttpResponse}

object RestDeserializer {
  trait AbstractDeserializer[T] {
    def apply(resp: HttpResponse): Option[T]

    def contentType: Option[String] = None

    protected def getEntity(resp: HttpResponse)(f: HttpEntity => Option[T]): Option[T] = {
      resp.getEntity() match {
        case e:HttpEntity => f(e)
        case _            => None
      }
    }

    protected def getBodyString(resp: HttpResponse)(f: String => Option[T]): Option[T] = {
      getEntity(resp) { entity =>
        val stringBody = EntityUtils.toString(entity)
        val result = f(stringBody)
        EntityUtils.consume(entity)
        result
      }
    }
  }

  trait ToString extends AbstractDeserializer[String] {
    def apply(resp: HttpResponse): Option[String] = {
      getBodyString(resp)((v) => Some(v))
    }
  }

  trait ToByteArray extends AbstractDeserializer[Array[Byte]] {
    def apply(resp: HttpResponse): Option[Array[Byte]] = {
      getEntity(resp) { entity =>
        val body = EntityUtils.toByteArray(entity)
        EntityUtils.consume(entity)
        Some(body)
      }
    }
  }

  trait ToXmlElem extends AbstractDeserializer[xml.Elem] {

    override def contentType = Some("application/xml")

    def apply(resp: HttpResponse): Option[xml.Elem] = {
      getBodyString(resp) { body =>
        try {
          Some(xml.XML.loadString(body))
        } catch {
          case e:org.xml.sax.SAXParseException => None
        }
      }
    }
  }

  trait ToJsonNode extends AbstractDeserializer[JsonNode] {

    lazy val mapper = new ObjectMapper

    override def contentType = Some("application/json")

    def apply(resp: HttpResponse): Option[JsonNode] = {
      getBodyString(resp) { body =>
        try {
          Some(ToJsonNodeDeserializer.mapper.readTree(body))
        } catch {
          case e:JsonMappingException => None
          case e:JsonParseException => None
        }
      }
    }
  }

  implicit object ToStringDeserializer extends ToString
  implicit object ToByteArrayDeserializer  extends ToByteArray
  implicit object ToXmlElemDeserializer extends ToXmlElem
  implicit object ToJsonNodeDeserializer extends ToJsonNode
}
