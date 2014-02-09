package actors

import scala.concurrent.duration._
import akka.actor.Actor


object AuctionAdministratorActor {

  sealed abstract class AuctionStatus(val displayName: String)
  case object COUNTDOWN extends AuctionStatus("Countdown")
  case object ORDER_ENTRY extends AuctionStatus("Order entry")
  case object MATCHING extends AuctionStatus("Matching")
  case object ENDED extends AuctionStatus("Ended")

  //CREATED, DELETED, CONFLICT, COUNTDOWN, ORDER_ENTRY, LOCK_DOWN, ORDER_IMPROVEMENT, ENDED, MATCHING

  case class AuctionPhase(phase: AuctionStatus, duration: Duration)

  case class Auction(name: String, current: AuctionStatus = ENDED, phases: List[AuctionPhase])

  case class CreateAuction(auction: Auction)

  case class DeleteAuction(auction: Auction)

  case class StartAuction(name: String)

  case class StopAuction(name: String)

  case class UpdateAck(result: Option[Auction])

  case class GetAuctions()

  case class GetAuctionsResult(auctions:  List[Auction])

}

class AucionAdministratorActor extends Actor {
  import AuctionAdministratorActor._

  var auctionList: List[Auction] = List()

  def changeCurrentStatus(auction: Auction, newStatus: AuctionStatus) =
    auctionList = auction.copy(current = newStatus) :: auctionList.filterNot(_.name == auction.name)

  def receive: Actor.Receive = {
    case CreateAuction(auction) =>
      auctionList = auction :: auctionList
      sender ! UpdateAck(Some(auction))
    case GetAuctions =>
      sender ! GetAuctionsResult(auctionList)
    case DeleteAuction(auction) =>
      auctionList = auctionList.filter(_ != auction)
      sender ! UpdateAck(Some(auction))
    case StartAuction(name) =>
      auctionList.find(_.name == name) match {
        case Some(auction) =>
          changeCurrentStatus(auction, COUNTDOWN)
          sender ! UpdateAck(Some(auction))
        case None =>
          sender ! UpdateAck(None)
      }
    case StopAuction(name) =>
      auctionList.find(_.name == name) match {
        case Some(auction) =>
          changeCurrentStatus(auction, ENDED)
          sender ! UpdateAck(Some(auction))
        case None =>
          sender ! UpdateAck(None)
      }
  }
}