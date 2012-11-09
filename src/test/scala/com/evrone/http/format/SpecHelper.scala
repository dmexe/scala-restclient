package com.evrone.http.response.format

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

import scala.io.Source
import org.apache.http.message.BasicHttpResponse
import org.apache.http.{HttpVersion,HttpStatus}
import org.apache.http.entity.{StringEntity,ByteArrayEntity}

trait SpecHelper { this: FunSpec =>
  def getHttpResponse = {
    new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK")
  }

  def getStringResponse(s: String) = {
    val entity = new StringEntity(s, "UTF-8")
    val httpResp = getHttpResponse
    httpResp.setEntity(entity)
    httpResp
  }

  def getByteArrayResponse(s: Array[Byte]) = {
    val entity = new ByteArrayEntity(s)
    val httpResp = getHttpResponse
    httpResp.setEntity(entity)
    httpResp
  }

  def resourceToString(name: String) = {
    Source.fromURL(getClass.getResource(name)).mkString
  }
}
