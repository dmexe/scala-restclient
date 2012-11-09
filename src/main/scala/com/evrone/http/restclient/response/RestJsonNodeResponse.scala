package com.evrone.http.restclient.response

import com.twitter.util.Try
import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper}
import org.apache.http.HttpResponse

object RestJsonNodeResponse extends RestResponse {
  lazy val mapper = new ObjectMapper

  def asJsonNode(resp: HttpResponse): Try[JsonNode] = {
    Try(mapper.readTree(getEntityString(resp)))
  }
}
