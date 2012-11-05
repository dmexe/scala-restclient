package com.evrone.restclient.http

import org.apache.http.util.EntityUtils
import org.apache.http.{HttpEntity,HttpResponse}

object Response {
  trait Deserializer[T] {
    def apply(resp: HttpResponse): Option[T]
  }

  trait StringDeserializer extends Deserializer[String] {
    def apply(resp: HttpResponse): Option[String] = {
      resp.getEntity() match {
        case e:HttpEntity => {
          val body = EntityUtils.toString(e)
          EntityUtils.consume(e)
          Some(body)
        }
        case _ => None
      }
    }
  }

  trait ByteArrayDeserializer extends Deserializer[Array[Byte]] {
    def apply(resp: HttpResponse): Option[Array[Byte]] = {
      resp.getEntity() match {
        case e:HttpEntity => {
          val body = EntityUtils.toByteArray(e)
          EntityUtils.consume(e)
          Some(body)
        }
        case _ => None
      }
    }
  }

  implicit object StringResponse extends StringDeserializer
  implicit object ByteArrayResponse extends ByteArrayDeserializer
}
