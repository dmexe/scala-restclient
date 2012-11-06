package com.evrone.restclient.response

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.{JsonNode,ObjectMapper,JsonMappingException}

trait RestResponseAsJsonNode extends RestResponseBuilder {
  def asJsonNode: Option[JsonNode] = {
    asString.flatMap { body =>
      try {
        Some(RestResponseAsJsonNode.mapper.readTree(body))
      } catch {
        case e:JsonMappingException => None
        case e:JsonParseException => None
      }
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
