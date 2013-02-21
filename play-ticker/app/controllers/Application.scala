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

object Application extends Controller {
  val myActor = Akka.system.actorOf(Props[MyActor], name = "myactor")
  
  def index = Action {
    myActor ! "test"
    Ok(views.html.index())
  }

}
