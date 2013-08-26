package models

import akka.actor._
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor.Props

class CalculatorActor extends Actor {
  import CalculatorActor._
  
	def receive = {
	  case CalculateEvent(expression) => {
	    sender ! calculate()
	  }
	  
	  def calculate(): Event = {
	   try {
		  ResultEvent("" + Calculator.apply(expression))
	    } catch {
	      case e: RuntimeException => ErrorEvent(e)	      
	    }		  
	  }
	}
  
  	
}

object CalculatorActor {
  trait Event
  case class CalculateEvent(expression: String) extends Event
  case class ResultEvent(result: String) extends Event
  case class ErrorEvent(exception: Exception) extends Event
  lazy val ref = Akka.system.actorOf(Props[CalculatorActor], name = "calculateactor")
}