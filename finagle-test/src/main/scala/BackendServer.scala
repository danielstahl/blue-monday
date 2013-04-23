
import com.twitter.finagle.builder.{ServerBuilder, Server}
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.util.Future
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol
import thrift._

/**
 *
 */
object BackendServer {
  def main(args: Array[String]) {
    val processor = new Hello.FutureIface {
      def hi(name: String) = Future.value("Hello %s, this is a message from the backend server".format(name))
    }

    val service = new Hello.FinagledService(processor, new TBinaryProtocol.Factory())

    val server: Server = ServerBuilder()
      .name("HelloBackend")
      .bindTo(new InetSocketAddress(8081))
      .codec(ThriftServerFramedCodec())
      .build(service)
  }


}
