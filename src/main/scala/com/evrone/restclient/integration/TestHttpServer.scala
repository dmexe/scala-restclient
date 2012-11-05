package com.evrone.restclient.integration

import com.sun.net.httpserver.{HttpExchange,HttpHandler,HttpServer,BasicAuthenticator}
import java.io.OutputStream
import java.net.InetSocketAddress
import java.util.logging.Logger
import scala.collection.JavaConversions._

case class HandleRequest(method:        Option[List[String]]          = None,
                         headers:       Option[Map[String,String]]    = None,
                         dataAsString:  Option[String]                = None,
                         queryString:   Option[String]                = None,
                         basicAuth:     Option[Tuple2[String,String]] = None) {
  def withMethod(x: List[String])         = copy(method       = Some(x))
  def withMethod(x: String)               = copy(method       = Some(List(x)))
  def withHeader(x: Map[String,String])   = copy(headers      = Some(x))
  def withHeader(n: String, v :String)    = copy(headers      = Some(Map(n -> v)))
  def withData(x: String)                 = copy(dataAsString = Some(x))
  def withQueryString(x :String)          = copy(queryString  = Some(x))
  def withBasicAuth(u :String, p :String) = copy(basicAuth    = Some(Tuple2(u,p)))
}

case class HandleResponse(dataAsString: String             = "",
                          code:         Int                = 200,
                          contentType:  String             = "text/html",
                          headers:      Map[String,String] = Map()) {
  def withData(x: String)               = copy(dataAsString = x)
  def withCode(x: Int)                  = copy(code = x)
  def withContentType(x: String)        = copy(contentType = x)
  def withHeader(x: Map[String,String]) = copy(headers = x)
  def withHeader(n :String, v:String)   = copy(headers = Map(n -> v))
}

class TestHttpHandler(val req: HandleRequest, val res: HandleResponse) extends HttpHandler {
  lazy val log = Logger.getLogger("TestHttpServer")

  def handle(x:HttpExchange) {
    val rs = for{
      method  <- reqMethod(x).right
      headers <- reqHeaders(x).right
      data    <- reqDataAsString(x).right
      query   <- reqQueryString(x).right
    } yield method

    rs match {
      case Right(_) => {
        val headers = x.getResponseHeaders()
        headers.add("Content-Type", res.contentType)
        for(value <- req.headers) {
          value.foreach((k) => headers.add(k._1,k._2))
        }
        response(x, res.code, res.dataAsString)
      }
      case Left(s)  => response(x, 499, s)
    }
  }

  private def response(x:HttpExchange, code:Int, body:String) {
    if (code == 499) {
      log.warning(body)
    }
    val os: OutputStream = x.getResponseBody()
    x.sendResponseHeaders(code, body.length)
    os.write(body.getBytes())
    os.close()
  }

  private def reqMethod(x:HttpExchange): Either[String,Int] = {
    req.method match {
      case Some(m) => {
        if(m.contains(x.getRequestMethod())) {
          Right(0)
        } else {
          Left("Invalid request method: expected " + m + " got " + x.getRequestMethod())
        }
      }
      case _ => Right(0)
    }
  }

  private def reqHeaders(x:HttpExchange): Either[String,Int] = {
    req.headers match {
      case Some(h) => {
        val headers = x.getRequestHeaders()
        val exists = h.forall { (k) =>
          headers.containsKey(k._1) && headers.get(k._1).contains(k._2)
        }
        if (exists) {
          Right(0)
        } else {
          Left("Invalid request headers: expected " + h + " got " + headers.toList)
        }
      }
      case _ => Right(0)
    }
  }

  private def reqDataAsString(x:HttpExchange): Either[String,Int] = {
    req.dataAsString match {
      case Some(d) => {
        val body = getBodyString(x)
        if(body == d) {
          Right(0)
        } else {
          Left("Invalid body: expected [" + d + "] got [" + body + "]")
        }
      }
      case _ => Right(0)
    }
  }

  private def reqQueryString(x:HttpExchange): Either[String,Int] = {
    req.queryString match {
      case Some(d) => {
        val body = getBodyString(x)
        if(body == d) {
          Right(0)
        } else {
          Left("Invalid body: expected [" + d + "] got [" + body + "]")
        }
      }
      case _ => Right(0)
    }
  }

  private def getBodyString(x:HttpExchange): String = {
    val s = new java.util.Scanner(x.getRequestBody()).useDelimiter("\\A")
    if(s.hasNext()) {
      s.next()
    } else ""
  }
}

class TestHttpBasicAuthenticator(val name:String, val pass:String) extends BasicAuthenticator("TestServer") {
  def checkCredentials(username:String, password:String):Boolean = {
    username == name && password == password
  }
}

class TestHttpServer {
  val server = HttpServer.create(new InetSocketAddress(0), 0)

  def handle(url:String)(f: (HandleRequest,HandleResponse) => (HandleRequest, HandleResponse) ) = {
    val reqAndRes = f(new HandleRequest, new HandleResponse)

    var context = server.createContext(url, new TestHttpHandler(reqAndRes._1, reqAndRes._2))
    reqAndRes._1.basicAuth match {
      case Some(x) => {
        context.setAuthenticator(new TestHttpBasicAuthenticator(x._1,x._2))
      }
      case _ => None
    }
    context
  }

  def removeContext(url:String) {
    server.removeContext(url)
  }

  def start() {
    server.setExecutor(null)
    server.start()
 }

  def stop() = server.stop(0)

  def address = "127.0.0.1:%s".format(server.getAddress().getPort())
}
