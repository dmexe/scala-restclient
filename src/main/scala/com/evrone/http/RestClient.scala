package com.evrone.http

import com.evrone.http.restclient.impl.RestRequest
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.PoolingClientConnectionManager
import org.apache.http.protocol.BasicHttpContext

class RestClient {
  val httpClient  = new DefaultHttpClient(RestClient.connManager)
  val httpContext = new BasicHttpContext()

  def get   (url: String) = build("GET",    url)
  def post  (url: String) = build("POST",   url)
  def head  (url: String) = build("HEAD",   url)
  def put   (url: String) = build("PUT",    url)
  def patch (url: String) = build("PATCH",  url)
  def delete(url: String) = build("DELETE", url)

  def build(method: String, url: String): RestRequest = {
    RestRequest(this, method, url)
  }
}

object RestClient {
  lazy val connManager = new PoolingClientConnectionManager()

  def close() = connManager.shutdown()

  def stats = connManager.getTotalStats().toString()
}
