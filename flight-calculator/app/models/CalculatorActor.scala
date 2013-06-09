package models

import akka.actor._
import akka.actor.Status._
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor.Props

class CalculatorActor extends Actor {
  import CalculatorActor._
  
	def receive = {
	  case CalculateEvent(expression) => {
	    try {
		  sender ! ResultEvent("" + Calculator.apply(expression))
	    } catch {
	      case e: RuntimeException => sender ! ErrorEvent(e)	      
	    }
	  }
	}
}

object CalculatorActor {
  trait Event
  case class CalculateEvent(expression: String)
  case class ResultEvent(result: String)
  case class ErrorEvent(exception: Exception)
  lazy val ref = Akka.system.actorOf(Props[CalculatorActor], name = "calculateactor")
}