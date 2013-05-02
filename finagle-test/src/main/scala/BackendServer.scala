
import com.twitter.finagle.redis.{Redis, Client}
import com.twitter.finagle.redis.util.{CBToString, StringToChannelBuffer}
import org.jboss.netty.buffer.ChannelBuffer
import thrift._
import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder, Server}
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.util.Future
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol


case class PersonProcessor(redisClient: Client) extends PersonStorage.FutureIface {
  def store(person: thrift.Person): Future[Unit] = {
    redisClient.hMSet(StringToChannelBuffer("person:" + person.`name`), personToMap(person))
    Future()
  }

  def retrieve(name: String): Future[thrift.Person] = {
    redisClient.hGetAll(StringToChannelBuffer("person:" + name)).map {
      futureResult =>
        futureResult match {
          case (Seq()) => {
            throw PersonStorageException(ErrorCode.NoSuchPersonError, name + " is not a valid person")
          }
          case _ => {
            tuplesToPerson(CBToString.fromTuples(futureResult))
          }
        }
    }
  }

  def personToMap(person: thrift.Person): Map[ChannelBuffer, ChannelBuffer] = {
    Map(
      "name" -> person.`name`,
      "age" -> ("" + person.`age`)
    ).map(tup => (StringToChannelBuffer(tup._1), StringToChannelBuffer(tup._2)))
  }

  def tuplesToPerson(tuples: Seq[(String, String)]): thrift.Person = {
    var theName: String = null
    var theAge: Int = 0
    tuples.foreach {
      tuple =>
        tuple match {
          case ("name", tmpName) => theName = tmpName
          case ("age", tmpAge) => theAge = tmpAge.toInt
        }
    }
    thrift.Person(theName, theAge)
  }

}


object PersonService {
  def main(args: Array[String]) {
    val redisClient: Client = Client(ClientBuilder()
      .hosts("localhost:6379")
      .hostConnectionLimit(1)
      .codec(Redis())
      .build())

    val service = new PersonStorage.FinagledService(PersonProcessor(redisClient), new TBinaryProtocol.Factory())
    val server: Server = ServerBuilder()
      .name("PersonBackend")
      .bindTo(new InetSocketAddress(8081))
      .codec(ThriftServerFramedCodec())
      .build(service)
  }
}
