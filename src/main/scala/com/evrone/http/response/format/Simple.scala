package com.evrone.http.response.format

import org.apache.http.HttpResponse
import org.apache.http.util.EntityUtils
import com.twitter.util.{Return,Try}

object asString extends (HttpResponse => Try[String]) with RestResponse {
  def apply(resp: HttpResponse): Try[String] = Return(getEntityString(resp))
}

object asByteArray extends (HttpResponse => Try[Array[Byte]]) with RestResponse {
  def apply(resp: HttpResponse): Try[Array[Byte]] = {
    val httpResp = getEntity(resp) { e =>
      e.map(EntityUtils.toByteArray) getOrElse Array[Byte]()
    }
    Return(httpResp)
  }
}
