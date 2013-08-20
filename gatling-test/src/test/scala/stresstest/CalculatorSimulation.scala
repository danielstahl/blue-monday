package stresstest

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._


/**
 * Stress test of the calculator
 */
class CalculatorSimulation extends Simulation {
  val httpProtocol = http.baseURL("http://localhost:9000")
  
		  
  val scn = scenario("Calculator scenario"). 
  	repeat(20) {
	    exec(http("Index page").
	      get("http://localhost:9000").
	      check(status is 200)).
	    pause(100 millis, 200 millis).
	    exec(http("Calculate 1 + 4").
	      get("http://localhost:9000/calculateAkka?expression=1%2B4").
	      check(status is 200)).
	    pause(50 millis, 300 millis).
	    exec(http("Error 6/").
	      get("http://localhost:9000/calculateAkka?expression=6%2F").
	      check(status is 400))
  	}
      

  setUp(scn.inject(ramp(60 users) over (40 seconds)))
  	.protocols(httpProtocol)
}
