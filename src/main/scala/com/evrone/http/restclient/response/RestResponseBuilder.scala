package com.evrone.http.restclient.response

import com.twitter.util.{Try,Throw,Return}
import org.apache.http.HttpResponse

trait RestResponseBuilder {
  def asString: Try[String]
  def asByteArray: Try[Array[Byte]]
}

case class HttpEntityDoesNotExists(msg: String = "HttpEntity does not exists") extends Exception(msg)

object RestResponseBuilder {
  trait Deserializer extends RestResponseBuilder
                        with RestResponseAs
                        with RestResponseAsJsonNode
                        with RestResponseAsXml

  case class Success(resp: Try[HttpResponse]) extends Deserializer {
    def asString = {
      resp.flatMap { resp =>
        Utils.getString(resp)(Some(_)) match {
          case Some(x:String) => Return[String](x)
          case None    => Throw[String](new HttpEntityDoesNotExists)
        }
      }
    }
    def asByteArray = {
      resp.flatMap { resp =>
        Utils.getByteArray(resp)(Some(_)) match {
          case Some(x:Array[Byte]) => Return[Array[Byte]](x)
          case None    => Throw[Array[Byte]](new HttpEntityDoesNotExists)
        }
      }
    }
  }

}
