package com.evrone.http.restclient.impl

import java.net.URI
import org.apache.http.auth.{AuthScope,UsernamePasswordCredentials}
import org.apache.http.client.methods.{HttpGet,HttpHead,HttpPost,HttpRequestBase}
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import scala.collection.JavaConversions._
import com.evrone.http.restclient.request.RestRequestBuilder

class HttpRequestBuilder(restReq: RestRequestBuilder) {
  val httpClient = restReq.client.httpClient

  def getRequest: HttpRequestBase = {
    val httpReq = restReq.method match {
      case "GET"  => GET
      case "HEAD" => HEAD
      case _      => POST
    }

    for((k,v) <- restReq.headers) httpReq.addHeader(k,v)

    for(x <- restReq.basicAuth) {
      httpClient.getCredentialsProvider.setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(x._1, x._2))
    }
    httpReq
  }

  def GET  = new HttpGet(uri)
  def HEAD = new HttpHead(uri)

  def POST = {
    val post = new HttpPost(uri) {
      override def getMethod = restReq.method
    }

    for(x <- restReq.postDataAsString) {
      val entity = new StringEntity(x._1)
      entity.setContentType(x._2)
      post.setEntity(entity)
    }

    val paramsPairs = restReq.params.flatMap((v) => List(new BasicNameValuePair(v._1,v._2)))
    if(!paramsPairs.isEmpty) {
      val urlEncodedEntity = new UrlEncodedFormEntity(paramsPairs.toList, "UTF-8")
      post.setEntity(urlEncodedEntity)
    }

    post
  }

  private def uri: URI = {
    val uriBuilder = new URIBuilder(restReq.url)
    for(q <- restReq.queryString) uriBuilder.setQuery(q)
    for((k,v) <- restReq.query) uriBuilder.addParameter(k,v)
    uriBuilder.build()
  }
}

object HttpRequestBuilder extends (RestRequestBuilder => HttpRequestBase) {
  def apply(restReq: RestRequestBuilder) = new HttpRequestBuilder(restReq).getRequest
}
