package com.evrone.http.restclient.impl

import org.apache.http.client.methods.{HttpGet,HttpHead,HttpPost,HttpRequestBase,HttpEntityEnclosingRequestBase}
import org.apache.http.HttpResponse
import java.io.IOException

object RestExecutor {
  def getResponse(client: com.evrone.http.RestClient, httpReq: HttpRequestBase): Either[String,HttpResponse] = {
    log(httpReq.getRequestLine())
    val http  = client.httpClient

    try {
      val httpRes = http.execute(httpReq, client.httpContext)
      log(httpRes.getStatusLine())

      httpRes.getStatusLine().getStatusCode() match {
        case ok if 200 until 299 contains ok => Right(httpRes)
        case _   => Left(httpRes.getStatusLine().toString())
      }
    } catch {
      case e: IOException => Left(e.getClass() + ": " + e.getMessage())
    }
  }

  private def log(s:Any) = true // println(s)
}
