package com.evrone.http.restclient.response

import org.apache.http.HttpResponse
import org.apache.http.util.EntityUtils
import com.twitter.util.{Return,Try}

object RestStringResponse extends RestResponse {
  def asString(resp: HttpResponse): Try[String] = Return(getEntityString(resp))
}

object RestByteArrayResponse extends RestResponse {
  def asByteArray(resp: HttpResponse): Try[Array[Byte]] = {
    val httpResp = getEntity(resp) { e =>
      e.map(EntityUtils.toByteArray) getOrElse Array[Byte]()
    }
    Return(httpResp)
  }
}
