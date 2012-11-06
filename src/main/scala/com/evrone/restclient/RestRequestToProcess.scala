package com.evrone.restclient

case class RestRequestToProcess(req: http.RestRequest) {
  import deserializer.RestDeserializer._

  def as[T](implicit deserializer: AbstractDeserializer[T]): Either[String,T] = {
    val httpReq = http.RestBuilder(req.withAccept(deserializer.contentType))
    val maybeHttpRes = http.RestExecutor.getResponse(req.client, httpReq)

    maybeHttpRes.right.flatMap { body =>
      deserializer(body) match {
        case Some(x) => Right(x)
        case None    => Left("Fail to process response")
      }
    }
  }

  def to[T](implicit mf: scala.reflect.Manifest[T]):Either[String,T] = {
    import org.json4s._
    import org.json4s.jackson.JsonMethods._

    val b = RestRequestToProcess(req.withHeader("Accept", "application/json")).as[String]

    val formats = DefaultFormats
    b match {
      case Right(x) => Right(parse(x).extract[T](formats, mf))
      case Left(s)  => Left(s)
    }
  }
}
