import com.twitter.finagle.http.path.{Root, Path}
import com.twitter.finagle.http.RichHttp
import org.apache.thrift.protocol.TBinaryProtocol
import org.jboss.netty.handler.codec.http.HttpMethod
import thrift.Hello
import com.twitter.finagle.thrift.{ThriftClientFramedCodec, ThriftClientRequest}
import com.twitter.finagle.{Server, Service}

import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder}
import com.twitter.finagle.http._
import com.twitter.util.Future
import java.net.InetSocketAddress
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.twitter.finagle.http.path._


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
        .codec(RichHttp[Request](Http()))
        .bindTo(new InetSocketAddress(8080))
        .name("httpserver")
        .build(new Respond(backendService))
    }
}

class Respond(backendService: Service[ThriftClientRequest, Array[Byte]]) extends Service[Request, Response] {
  def apply(req: Request): Future[Response] = {
    (req.method -> Path(req.path)) match {
      case (HttpMethod.GET -> Root / "hello" / name) => {
        val backendClient = new Hello.FinagledClient(backendService, new TBinaryProtocol.Factory())
        backendClient.hi(name) map { backendClientResponse =>
          val response = Response()
          response.setContent(copiedBuffer(backendClientResponse, UTF_8))
          response
        }
      }
      case _ =>
        Future value Response(Version.Http11, Status.NotFound)
    }
  }
}


