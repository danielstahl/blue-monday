import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finagle.http.path./
import com.twitter.finagle.http.path.{Root, Path}
import com.twitter.finagle.http.RichHttp
import org.apache.thrift.protocol.TBinaryProtocol
import org.codehaus.jackson.map.ObjectMapper
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.HttpMethod
import _root_.thrift._
import com.twitter.finagle.thrift.{ThriftClientFramedCodec, ThriftClientRequest}
import com.twitter.finagle.{Server, Service}

import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder}
import com.twitter.finagle.http._
import com.twitter.util.Future
import java.net.InetSocketAddress
import com.twitter.finagle.http.path._


/**
 *
 */
object RestServer {
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
  private val mapper = makeMapper

  def makeMapper: ObjectMapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper
  }


  def apply(req: Request): Future[Response] = {
    (req.method -> Path(req.path)) match {
      case (HttpMethod.GET -> Root / "person" / name) => {
        val personClient = new PersonStorage.FinagledClient(backendService, new TBinaryProtocol.Factory())
        personClient.retrieve(name) map {
          backendClientResponse =>
            val response = Response()
            response.setContentTypeJson()
            response.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"))
            response.content = ChannelBuffers.copiedBuffer(mapper.writeValueAsBytes(backendClientResponse))
            response
        } rescue {
          case e: PersonStorageException  => {
            Future(Response(Version.Http11, Status.BadRequest))
          }
          case _ => {
            Future(Response(Version.Http11, Status.InternalServerError))
          }

        }
      }
      case (HttpMethod.POST -> Root / "person") => {
        val personClient = new PersonStorage.FinagledClient(backendService, new TBinaryProtocol.Factory())
        val thePerson = thrift.Person(req.getParam("name"), req.getIntParam("age"))
        personClient.store(thePerson)
        val response = Response()
        Future(response)
      }

      case _ =>
        Future(Response(Version.Http11, Status.NotFound))
    }
  }
}


