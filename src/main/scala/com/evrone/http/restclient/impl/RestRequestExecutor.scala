package com.evrone.http.restclient.impl

import com.twitter.util.{Try,Throw}
import org.apache.http.HttpResponse
import com.evrone.http.restclient.request.RestRequestBuilder

class RestRequestExecutor(restReq: RestRequestBuilder) {

  def execute[T](f: HttpResponse => Try[T]): Try[T] = {
    restReq.hasError match {
      case None => {
        val r = HttpRequestBuilder andThen HttpRequestExecutor(restReq.client)
        r(restReq).andThen(f)
      }
      case Some(e) => Throw[T](e)
    }
  }

  def andThen = execute _

}


