package com.evrone.http.response.format

import com.twitter.util.Try
import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper}
import org.apache.http.HttpResponse

object asJsonNode extends (HttpResponse => Try[JsonNode]) with RestResponse {
  lazy val mapper = new ObjectMapper

  def apply(resp: HttpResponse): Try[JsonNode] = {
    Try(mapper.readTree(getEntityString(resp)))
  }
}
