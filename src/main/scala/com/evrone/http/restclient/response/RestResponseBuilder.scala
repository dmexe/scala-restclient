package com.evrone.http.restclient.response

import org.apache.http.HttpResponse

trait RestResponseBuilder {
  def asString: Option[String]
  def asByteArray: Option[Array[Byte]]
}

object RestResponseBuilder {
  trait Deserializer extends RestResponseBuilder
                        with RestResponseAs
                        with RestResponseAsJsonNode
                        with RestResponseAsXml

  case class Success(resp: HttpResponse) extends Deserializer {
    def asString = Utils.getString[String](resp)(Some(_))
    def asByteArray = Utils.getByteArray(resp)(Some(_))
  }

  case class Fail(error: String) extends Deserializer {
    def asString = None
    def asByteArray = None
  }
}
