package com.evrone.http.restclient.impl

import com.evrone.http.restclient.response.RestResponseBuilder
import com.twitter.util.{Try,Return,Throw}
import org.apache.http.HttpResponse

trait RestRequestAndThen { self: RestRequest =>
  def andThen = {
    maybeHttpResponse match {
      case Return(x) => RestResponseBuilder.Success(Return(x))
      case Throw(x)  => RestResponseBuilder.Success(Throw(x))
    }
  }

  def andThen[T](f: HttpResponse => Option[T]): Option[T] = {
    maybeHttpResponse match {
      case Return(x) => f(x)
      case Throw(x)  => None
    }
  }

  private def maybeHttpResponse: Try[HttpResponse] = {
    val httpReq = RestBuilder(self)
    RestExecutor.getResponse(client, httpReq)
  }
}

case class RestRequest(client:      com.evrone.http.RestClient,
                       method:      String,
                       url:         String,
                       query:       Map[String,String] = Map(),
                       queryString: Option[String]     = None,
                       headers:     Map[String,String] = Map(),
                       params:      Map[String,String] = Map(),
                       basicAuth:   Option[Tuple2[String,String]] = None,
                       postData:    Option[Tuple2[String,String]] = None) extends RestRequestAndThen {

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
}
