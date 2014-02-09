package actors

import org.scalatest.{BeforeAndAfterAll, FunSuiteLike, Matchers, FunSuite}
import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import actors.AuctionAdministratorActor._
import scala.concurrent.duration._

class AuctionAdministratorActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with FunSuiteLike with Matchers with BeforeAndAfterAll {
  def this() = this(ActorSystem("TestSys"))

  test("Create a auction") {
    val administrator = system.actorOf(Props[AucionAdministratorActor])

    val auction = Auction(name = "auction 1", phases = List())
    administrator ! CreateAuction(auction)
    expectMsg(UpdateAck(Some(auction)))
  }

  test("Get auctions") {
    val administrator = system.actorOf(Props[AucionAdministratorActor])

    val auction = Auction(name = "auction 1", phases = List())
    administrator ! CreateAuction(auction)
    expectMsg(UpdateAck(Some(auction)))

    administrator ! GetAuctions
    expectMsg(GetAuctionsResult(List(auction)))
  }

  test("Remove auction") {
    val administrator = system.actorOf(Props[AucionAdministratorActor])

    val auction = Auction(name = "auction 1", phases = List())
    administrator ! CreateAuction(auction)
    expectMsg(UpdateAck(Some(auction)))

    administrator ! GetAuctions
    expectMsg(GetAuctionsResult(List(auction)))

    administrator ! DeleteAuction(auction)
    expectMsg(UpdateAck(Some(auction)))
    administrator ! GetAuctions
    expectMsg(GetAuctionsResult(List()))
  }

  override def afterAll: Unit = system.shutdown()
}
