package com.evrone.restclient.http

case class RestRequest(client:      RestClient,
                       method:      String,
                       url:         String,
                       query:       Map[String,String] = Map(),
                       queryString: Option[String]     = None,
                       headers:     Map[String,String] = Map(),
                       params:      Map[String,String] = Map(),
                       basicAuth:   Option[Tuple2[String,String]] = None,
                       postData:    Option[Tuple2[String,String]] = None) {

  type Hash = Map[String,String]

  def withMethod(x: String)            = copy(method = x)
  def withQuery (x: Hash)              = copy(query = query ++ x)
  def withQuery (k: String, v:String)  = copy(query = query ++ Map(k -> v))
  def withQuery (q: String)            = copy(queryString = Some(q))
  def withHeader(x: Hash)              = copy(headers = headers ++ x)
  def withParam (x: Hash)              = copy(params = params ++ x)
  def withParam (k: String, v: String) = copy(params = params ++ Map(k -> v))
  def withHeader(k: String, v: String) = copy(headers = headers ++ Map(k -> v))

  def withAccept(k: Option[String]) = {
    k match {
      case Some(a) => withHeader("Accept", a)
      case None    => this
    }
  }

  def withBasicAuth(user: String, password: String) = {
    copy(basicAuth = Some(user, password))
  }

  def withData(data: String, contentType: String) = {
    copy(postData = Some(data, contentType))
  }
}
