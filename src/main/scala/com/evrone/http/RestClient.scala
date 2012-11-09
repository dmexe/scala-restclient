package com.evrone.http

import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.PoolingClientConnectionManager
import org.apache.http.protocol.BasicHttpContext
import com.evrone.http.restclient.request.RestRequestBuilder

class RestClient(val httpClient:  DefaultHttpClient = RestClient.defaultHttpClient,
                 val httpContext: BasicHttpContext  = RestClient.defaultHttpContext,
                 val log:         (String => Any)   = RestClient.defaultLogger) {

  def this(log: String => Any) = {
    this(RestClient.defaultHttpClient, RestClient.defaultHttpContext, log)
  }

  def this(httpClient: DefaultHttpClient) = {
    this(httpClient, RestClient.defaultHttpContext, RestClient.defaultLogger)
  }

  def get   (url: String) = build("GET",    url)
  def post  (url: String) = build("POST",   url)
  def head  (url: String) = build("HEAD",   url)
  def put   (url: String) = build("PUT",    url)
  def patch (url: String) = build("PATCH",  url)
  def delete(url: String) = build("DELETE", url)

  def build(method: String, url: String): RestRequestBuilder = {
    RestRequestBuilder(this, method, url)
  }
}

object RestClient {
  lazy val connManager = new PoolingClientConnectionManager()

  def close() {
    connManager.shutdown()
  }

  def stats = connManager.getTotalStats.toString

  def defaultLogger(s:String) {}
  def defaultHttpClient  = new DefaultHttpClient(connManager)
  def defaultHttpContext = new BasicHttpContext()
}

package object restclient {
  import com.evrone.http.restclient.impl.RestRequestExecutor
  import com.twitter.util.Try
  import org.apache.http.HttpResponse

  type RestClient = com.evrone.http.RestClient

  implicit def RestRequestToHttpRequest(restReq: RestRequestBuilder) = {
    new RestRequestExecutor(restReq)
  }
}
