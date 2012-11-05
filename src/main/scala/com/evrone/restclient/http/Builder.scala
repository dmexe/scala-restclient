package com.evrone.restclient.http

import java.net.URI
import org.apache.http.auth.{AuthScope,UsernamePasswordCredentials}
import org.apache.http.client.methods.{HttpGet,HttpHead,HttpPost,HttpRequestBase,HttpEntityEnclosingRequestBase}
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.util.EntityUtils
import scala.collection.JavaConversions._

object Builder {

  def apply(req: Request) = {
    val httpReq = req.method match {
      case "GET"  => GET(req)
      case "HEAD" => HEAD(req)
      case _      => POST(req)
    }

    for((k,v) <- req.headers) httpReq.addHeader(k,v)

    for(x <- req.basicAuth) {
      req.client.httpClient.getCredentialsProvider().setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(x._1, x._2))
    }
    httpReq
  }

  def GET (req: Request) = new HttpGet(uri(req))
  def HEAD(req: Request) = new HttpHead(uri(req))

  def POST(req: Request) = {
    val post = new HttpPost(uri(req)) {
      override def getMethod() = req.method
    }

    for(x <- req.postData) {
      val entity = new StringEntity(x._1)
      entity.setContentType(x._2)
      post.setEntity(entity)
    }

    val paramsPairs = req.params.flatMap((v) => List(new BasicNameValuePair(v._1,v._2)))
    if(!paramsPairs.isEmpty) {
      val urlEncodedEntity = new UrlEncodedFormEntity(paramsPairs.toList, "UTF-8")
      post.setEntity(urlEncodedEntity)
    }

    post
  }

  private def uri(req: Request): URI = {
    val uriBuilder = new URIBuilder(req.url)
    for(q <- req.queryString) uriBuilder.setQuery(q)
    for((k,v) <- req.query) uriBuilder.addParameter(k,v)
    uriBuilder.build()
  }
}
