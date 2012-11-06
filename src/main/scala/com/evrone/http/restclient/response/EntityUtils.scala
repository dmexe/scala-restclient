package com.evrone.http.restclient.response

import org.apache.http.util.EntityUtils
import org.apache.http.{HttpEntity,HttpResponse}

object Utils {

  def getEntity[T](resp: HttpResponse)(f: HttpEntity => Option[T]): Option[T] = {
    resp.getEntity() match {
      case e:HttpEntity => f(e)
      case _            => None
    }
  }

  def getString[T](resp: HttpResponse)(f: String => Option[T]): Option[T] = {
    getEntity(resp) { entity =>
      val body = EntityUtils.toString(entity)
      val result = f(body)
      EntityUtils.consume(entity)
      result
    }
  }

  def getByteArray[T](resp: HttpResponse)(f: Array[Byte] => Option[T]): Option[T] = {
    getEntity(resp) { entity =>
      val body = EntityUtils.toByteArray(entity)
      val result = f(body)
      EntityUtils.consume(entity)
      result
    }
  }
}
