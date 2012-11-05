package com.evrone.restclient

import org.apache.http.util.EntityUtils
import org.apache.http.{HttpEntity,HttpResponse}

object Response {
  trait Deserializer[T] {
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

  trait StringDeserializer extends Deserializer[String] {
    def apply(resp: HttpResponse): Option[String] = {
      getBodyString(resp)((v) => Some(v))
    }
  }

  trait ByteArrayDeserializer extends Deserializer[Array[Byte]] {
    def apply(resp: HttpResponse): Option[Array[Byte]] = {
      getEntity(resp) { entity =>
        val body = EntityUtils.toByteArray(entity)
        EntityUtils.consume(entity)
        Some(body)
      }
    }
  }

  trait XmlElemDeserializer extends Deserializer[xml.Elem] {

    override def contentType = Some("application/json")

    def apply(resp: HttpResponse): Option[xml.Elem] = {
      getBodyString(resp) { body =>
        try {
          val x = xml.XML.loadString(body)
          Some(x)
        } catch {
          case e:org.xml.sax.SAXParseException => None
        }
      }
    }
  }

  implicit object StringResponse extends StringDeserializer
  implicit object ByteArrayResponse extends ByteArrayDeserializer
  implicit object XmlElemResponse extends XmlElemDeserializer
}
