package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import views.html.defaultpages.badRequest
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor.Props
import models.MyActor
import play.api.mvc.WebSocket
import play.api.libs.iteratee.Iteratee
import play.api.libs.iteratee.Enumerator

object Application extends Controller {
  val myActor = Akka.system.actorOf(Props[MyActor], name = "myactor")
  
  def index = Action {
    myActor ! "test"
    Ok(views.html.index())
  }

  def ticker = WebSocket.using[String] {
    request =>
	  val in = Iteratee.foreach[String](println).mapDone {
	    _ => println("Disconnected")
	  }
	  val out = Enumerator("Hello from play")
	  
	  (in, out)
  }
}
