package com.evrone.http.restclient.impl

import com.twitter.util.{Try,Throw,Return}
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write

trait RestRequestBodyAsJson[T] { self =>
  def withJson(v: AnyRef)(implicit formats:Formats = Serialization.formats(NoTypeHints)): T = {
    Try(write(v)).map(withData(_, "application/json")) match {
      case Return(x) => x
      case Throw(e)  => setError(e)
    }
  }

  def withData(data: String, contentType: String): T
  def resetData: T
  def setError(e: Throwable): T
  def client: com.evrone.http.RestClient
}
