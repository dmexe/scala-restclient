package com.evrone.http.restclient.response

import org.apache.http.util.EntityUtils
import org.apache.http.{HttpEntity, HttpResponse}
import com.twitter.util.{Throw, Return, Try}

trait RestResponse {

  def asString(resp: HttpResponse) = Return(getEntityString(resp))

  def asByteArray(resp: HttpResponse): Array[Byte] = {
    getEntity(resp) { e =>
      e.map(EntityUtils.toByteArray) getOrElse Array[Byte]()
    }
  }

  protected def getEntity[T](resp: HttpResponse)(f: Option[HttpEntity] => T): T = {
    resp.getEntity match {
      case e:HttpEntity => {
        val result = f(Some(e))
        EntityUtils.consume(e)
        result
      }
      case _ => f(None)
    }
  }

  protected def getEntityString(resp: HttpResponse): String = {
    getEntity(resp) { e =>
      e.map(EntityUtils.toString(_, "UTF-8")) getOrElse ""
    }
  }
}
