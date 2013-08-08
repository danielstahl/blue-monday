package controllers.test

import play.api.test._
import play.api.test.Helpers._
import org.scalatest.FunSpec

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
class IntegrationSpec extends FunSpec {
  
  describe("A route") {
	  it("Should route /service/calculate should route to Application.calculate") {
	    running(FakeApplication()) {
		    val Some(result) = route(FakeRequest(GET, "/service/calculate?expression=7"))
	    	assert(status(result) === OK)
	    	assert(contentAsString(result) === "7.0")
	    }
	  }
    
  }
}