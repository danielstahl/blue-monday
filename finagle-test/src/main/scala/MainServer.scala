import org.apache.thrift.protocol.TBinaryProtocol
import thrift.Hello
import com.twitter.finagle.thrift.{ThriftClientFramedCodec, ThriftClientRequest}
import com.twitter.finagle.{Server, Service}

import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder}
import com.twitter.finagle.http.Http
import com.twitter.util.{Await, Future}
import java.net.InetSocketAddress
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.util.CharsetUtil.UTF_8

/**
 *
 */
object MainServer {
  def main(args: Array[String]) {
      val backendService: Service[ThriftClientRequest, Array[Byte]] = ClientBuilder()
        .hosts(new InetSocketAddress(8081))
        .codec(ThriftClientFramedCodec())
        .hostConnectionLimit(1)
        .build()

      val theServer = ServerBuilder()
        .codec(Http())
        .bindTo(new InetSocketAddress(8080))
        .name("httpserver")
        .build(new Respond(backendService))
    }
}

class Respond(backendService: Service[ThriftClientRequest, Array[Byte]]) extends Service[HttpRequest, HttpResponse] {
  def apply(req: HttpRequest): Future[HttpResponse] = {
    val backendClient = new Hello.FinagledClient(backendService, new TBinaryProtocol.Factory())

    backendClient.hi("Frontend") map { backendClientResponse =>
      val response = new DefaultHttpResponse(req.getProtocolVersion, HttpResponseStatus.OK)
      response.setContent(copiedBuffer(backendClientResponse, UTF_8))
      response
    }
  }
}


