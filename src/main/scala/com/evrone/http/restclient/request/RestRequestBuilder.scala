package com.evrone.http.restclient.request

case class RestRequestBuilder(client:           com.evrone.http.RestClient,
                              method:           String,
                              url:              String,
                              query:            Map[String,String]       = Map(),
                              queryString:      Option[String]           = None,
                              headers:          Map[String,String]       = Map(),
                              params:           Map[String,String]       = Map(),
                              basicAuth:        Option[(String, String)] = None,
                              postDataAsString: Option[(String, String)] = None,
                              hasError:         Option[Throwable]        = None)
  extends RestRequestBuilderAsJson[RestRequestBuilder] {

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
    copy(postDataAsString = Some(data, contentType))
  }

  protected def setError(e: Throwable) = copy(hasError = Some(e))

}
