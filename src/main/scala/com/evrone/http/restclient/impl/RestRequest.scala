package com.evrone.http.restclient.impl

import com.evrone.http.restclient.response.RestResponseBuilder
import com.twitter.util.{Try,Return,Throw}
import org.apache.http.HttpResponse

trait RestRequestAndThen { self: RestRequest =>
  def andThen = RestResponseBuilder(maybeHttpResponse)

  def andThen[T](f: HttpResponse => Try[T]): Try[T] = {
    maybeHttpResponse.flatMap(f(_))
  }

  private def maybeHttpResponse: Try[HttpResponse] = {
    hasError match {
      case None => {
        val httpReq = RestBuilder(self)
        RestExecutor.getResponse(client, httpReq)
      }
      case Some(e) => Throw(e)
    }
  }
}

case class RestRequest(client:      com.evrone.http.RestClient,
                       method:      String,
                       url:         String,
                       query:       Map[String,String] = Map(),
                       queryString: Option[String]     = None,
                       headers:     Map[String,String] = Map(),
                       params:      Map[String,String] = Map(),
                       basicAuth:   Option[(String, String)] = None,
                       postData:    Option[(String, String)] = None,
                       hasError:    Option[Throwable]             = None) extends RestRequestAndThen
                                                                             with RestRequestBodyAsJson[RestRequest] {

  type Hash = Map[String,String]

  def withMethod(x: String)            = copy(method = x)
  def withQuery (x: Hash)              = copy(query = query ++ x)
  def withQuery (k: String, v:String)  = copy(query = query ++ Map(k -> v))
  def withQuery (q: String)            = copy(queryString = Some(q))
  def withHeader(x: Hash)              = copy(headers = headers ++ x)
  def withParam (x: Hash)              = copy(params = params ++ x)
  def withParam (k: String, v: String) = copy(params = params ++ Map(k -> v))
  def withHeader(k: String, v: String) = copy(headers = headers ++ Map(k -> v))
  def withAccept(k: String)            = withHeader("Accept", k)

  def withBasicAuth(user: String, password: String) = {
    copy(basicAuth = Some(user, password))
  }

  def withData(data: String, contentType: String) = {
    copy(postData = Some(data, contentType))
  }

  def resetData = copy(postData = None)
  def setError(e: Throwable) = copy(hasError = Some(e))
}
