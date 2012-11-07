package com.evrone.http.restclient.response

import com.twitter.util.{Try,Throw,Return}
import org.apache.http.HttpResponse

trait RestResponseBuilderBase {
  def asString: Try[String]
  def asByteArray: Try[Array[Byte]]
}

case class RestResponseBuilder(httpResp: Try[HttpResponse])
           extends RestResponseBuilderBase
           with RestResponseAsJson
           with RestResponseAsJsonNode
           with RestResponseAsXml {

  def asString = {
    httpResp.flatMap { resp =>
      Utils.getString(resp)(Some(_)) match {
        case Some(x:String) => Return[String](x)
        case None    => Throw[String](new HttpEntityDoesNotExists)
      }
    }
  }

  def asByteArray = {
    httpResp.flatMap { resp =>
      Utils.getByteArray(resp)(Some(_)) match {
        case Some(x:Array[Byte]) => Return[Array[Byte]](x)
        case None    => Throw[Array[Byte]](new HttpEntityDoesNotExists)
      }
    }
  }
}

case class HttpEntityDoesNotExists(msg: String = "HttpEntity does not exists") extends Exception(msg)
