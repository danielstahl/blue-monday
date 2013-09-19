import akka.actor.{Props, ActorSystem, Actor}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.language.postfixOps

class ClientMessage(uuid: String)

case class OrderMessage(orderbook: String, volume: Long, price: Double)

case class OrderStatusMessage(orderbook: String, volume: Long, price: Double)

case class ClientLogout(uuid: String) extends ClientMessage(uuid)

case class ClientLogin(uuid: String) extends ClientMessage(uuid)

trait CommonActor {
  actor: Actor =>
  def commonReceive: Actor.Receive = {
    case default =>
      println(s"${actor.self} does not understand: $default")
  }

  def myReceive: Actor.Receive

  def receive = myReceive orElse commonReceive
}


class ClientSupervisorActor extends Actor with CommonActor {
  def myReceive = {
    case clientLogin@ClientLogin(uuid) =>
      context.child(uuid).getOrElse {
        context.actorOf(Props[ClientActor](new ClientActor(uuid)), uuid)
      } forward clientLogin
    case orderStatusMessage: OrderStatusMessage =>
      context.children foreach (_ ! orderStatusMessage)

    case clientLogout@ClientLogout(uuid) =>
      context.child(uuid).foreach(_.forward(clientLogout))
  }
}

class ClientActor(uuid: String) extends Actor with CommonActor {

  def myReceive = {
    case osm: OrderStatusMessage =>
      println(s"Client $uuid got orderbook status $osm")
    case ClientLogin(uuid) =>
      println(s"Client login as ${uuid}")
    case ClientLogout(uuid) =>
      println(s"Client ${uuid} in actor ${self} is logging out")
      context.stop(self)
  }
}

class MarketActor extends Actor with CommonActor {
  def myReceive = {
    case orderMessage@OrderMessage(orderbook, volume, price) =>
      context.child(orderbook).getOrElse {
        context.actorOf(Props[OrderbookActor](new OrderbookActor(orderbook)))
      } forward orderMessage
  }
}

class OrderbookActor(orderbook: String) extends Actor with CommonActor {
  lazy val clientSupervisorActor = context.actorSelection("/user/clientSupervisorActor")

  def myReceive = {
    case orderMessage@OrderMessage(orderbook, volume, price) =>
      println(s"Orderbook $orderbook got update $orderMessage")
      clientSupervisorActor ! OrderStatusMessage(orderbook, volume, price)
  }
}

object ClientMain {
  val system = ActorSystem("OrderSystem")
  val clientSupervisor = system.actorOf(Props[ClientSupervisorActor], "clientSupervisorActor")
  val marketActor = system.actorOf(Props[MarketActor], "marketActor")

  def main(args: Array[String]) {
    val client1 = "client1"
    implicit val timeout = Timeout(5 seconds)
    clientSupervisor ! ClientLogin(client1)
    marketActor ! OrderMessage("ERIC-B", 100, 10.4)
    Thread.sleep(100)

    val client2 = "client2"
    clientSupervisor ! ClientLogin(client2)
    marketActor ! OrderMessage("ABB", 200, 10.4)
    Thread.sleep(100)

    marketActor ! OrderMessage("ERIC-B", 300, 11.5)
    Thread.sleep(100)

    clientSupervisor ! ClientLogout(client1)
    Thread.sleep(100)

    marketActor ! OrderMessage("ABB", 400, 14.8)
    Thread.sleep(100)

    clientSupervisor ! ClientLogout(client2)
    Thread.sleep(20000)

    system.shutdown()
  }
}

object RemoteMain {
  def main(args: Array[String]) = {
    System.setProperty("akka.remote.netty.tcp.port", "2553")
    val system = ActorSystem("OrderSystem")

    Thread.sleep(20000)

    system.shutdown()
  }
}
