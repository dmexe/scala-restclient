package com.evrone.http.restclient.impl

import org.apache.http.client.methods.{HttpGet,HttpHead,HttpPost,HttpRequestBase,HttpEntityEnclosingRequestBase}
import org.apache.http.HttpResponse
import java.io.IOException
import com.twitter.util.{Try,Throw}

object RestExecutor {
  def getResponse(client: com.evrone.http.RestClient, httpReq: HttpRequestBase): Try[HttpResponse] = {
    client.log(httpReq.getRequestLine().toString)
    val http  = client.httpClient

    Try {
      val httpRes = http.execute(httpReq, client.httpContext)
      client.log(httpRes.getStatusLine().toString)

      httpRes.getStatusLine().getStatusCode() match {
        case ok if 200 until 299 contains ok => httpRes
        case _ => throw new UnexpectedResponse(httpRes.getStatusLine().toString())
      }
    }
  }
}

case class UnexpectedResponse(msg: String) extends Exception(msg)
