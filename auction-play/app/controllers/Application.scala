package controllers

import play.api.Play.current
import play.api.mvc._
import akka.actor.Props
import play.api.libs.concurrent.Akka
import actors.AucionAdministratorActor
import play.api.libs.json._
import actors.AuctionAdministratorActor._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import actors.AuctionAdministratorActor.GetAuctionsResult
import actors.AuctionAdministratorActor.AuctionPhase
import actors.AuctionAdministratorActor.Auction
import actors.AuctionAdministratorActor.GetAuctions


object Application extends Controller {
  val auctionAdministrator = setupAuctionActor()

  def setupAuctionActor() = {
    val auctionActor = Akka.system.actorOf(Props[AucionAdministratorActor])
    auctionActor ! CreateAuction(Auction("ERIC-B auction", ORDER_ENTRY, List(AuctionPhase(COUNTDOWN, 1.minute), AuctionPhase(ORDER_ENTRY, 15.minute), AuctionPhase(ENDED, 0.minute))))
    auctionActor ! CreateAuction(Auction("BUND rolls", ENDED, List(AuctionPhase(COUNTDOWN, 1.minute), AuctionPhase(ORDER_ENTRY, 15.minute), AuctionPhase(ENDED, 0.minute))))
    auctionActor
  }

  implicit val phaseWriter = new Writes[AuctionPhase] {
    def writes(p: AuctionPhase): JsValue = {
      Json.obj(
        "phase" -> p.phase.displayName,
        "duration" -> p.duration.toMinutes
      )
    }
  }

  implicit val auctionWriter = new Writes[Auction] {
    def writes(a: Auction): JsValue = {
      Json.obj(
        "name" -> a.name,
        "status" -> a.current.displayName,
        "phases" -> Json.arr(a.phases)
      )
    }
  }

  def index = Action {
    Ok(views.html.index())
  }


  def auctions = Action.async {
    implicit val timeout = Timeout(5.seconds)
    (auctionAdministrator ? GetAuctions).mapTo[GetAuctionsResult].map {
      result => Ok(Json.toJson(result.auctions))
    }
  }

}