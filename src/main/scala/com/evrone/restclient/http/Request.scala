package com.evrone.restclient.http

case class Request(client:      RestClient,
                   method:      String,
                   url:         String,
                   query:       Map[String,String] = Map(),
                   queryString: Option[String]     = None,
                   headers:     Map[String,String] = Map(),
                   params:      Map[String,String] = Map(),
                   basicAuth:   Option[Tuple2[String,String]] = None,
                   postData:    Option[Tuple2[String,String]] = None) {

  type Hash = Map[String,String]

  def withMethod(x:String): Request = copy(method = x)

  def withQuery(x: Hash): Request = copy(query = query ++ x)

  def withQuery(k:String, v:String) = copy(query = query ++ Map(k -> v))

  def withQuery(q: String): Request = copy(queryString = Some(q))

  def withHeader(x: Hash): Request = copy(headers = headers ++ x)

  def withHeader(k: String, v: String): Request = {
    copy(headers = headers ++ Map(k -> v))
  }

  def withAccept(k: Option[String]): Request = {
    k match {
      case Some(a) => withHeader("Accept", a)
      case None    => this
    }
  }

  def withParams(x: Hash): Request = copy(params = params ++ x)

  def withParams(k: String, v: String): Request = {
    copy(params = params ++ Map(k -> v))
  }

  def withBasicAuth(user: String, password: String): Request = {
    copy(basicAuth = Some(user, password))
  }

  def withData(data: String, contentType: String): Request = {
    copy(postData = Some(data, contentType))
  }
}
