package com.evrone.http.restclient.response

import com.twitter.util.Try
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper,JsonMappingException}

trait RestResponseAsJsonNode extends RestResponseBuilderBase {
  def asJsonNode: Try[JsonNode] = {
    asString.flatMap { body =>
        Try(RestResponseAsJsonNode.mapper.readTree(body))
    }
  }
}

object RestResponseAsJsonNode {
  lazy val mapper = new ObjectMapper
}

/*
case e:JsonMappingException => None
case e:JsonParseException => None
*/
