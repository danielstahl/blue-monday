package models

import akka.actor.Actor
import akka.event.Logging

class MyActor extends Actor {
	val log = Logging(context.system, this)
	
	def receive = {
	  case "test" => log.info("test received")
	  case _ => log.info("received a unknown message")
	}
}