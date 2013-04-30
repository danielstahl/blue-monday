
import java.util.concurrent.ConcurrentHashMap
import scala.collection.mutable
import thrift._
import com.twitter.finagle.builder.{ServerBuilder, Server}
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.util.Future
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol
import collection.JavaConversions._

object PersonService {
  val personMap :mutable.ConcurrentMap[String, thrift.Person] = new ConcurrentHashMap[String, thrift.Person]

  def main(args: Array[String]) {
    val personProcessor = new PersonStorage.FutureIface {
      def store(person: thrift.Person): Future[Unit] = {
        personMap.put(person.`name`, person)
        Future()
      }

      def retrieve(name: String): Future[thrift.Person] = {
        personMap.get(name) match {
          case Some(person) => Future(person)
          case None => throw PersonStorageException(ErrorCode.NoSuchPersonError, name + " is not a valid person")
        }

      }
    }

    val service = new PersonStorage.FinagledService(personProcessor, new TBinaryProtocol.Factory())
    val server: Server = ServerBuilder()
      .name("PersonBackend")
      .bindTo(new InetSocketAddress(8081))
      .codec(ThriftServerFramedCodec())
      .build(service)
  }
}
