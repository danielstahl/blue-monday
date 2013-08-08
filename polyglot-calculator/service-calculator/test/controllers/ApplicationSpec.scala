package test.controllers

import play.api.test._
import play.api.test.Helpers._
import org.scalatest.FunSpec

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends FunSpec {
  
  describe("A calculate controller") {
    it("Should respond with 7 for input 7") {
    	val result = controllers.Application.calculate()(FakeRequest(GET, "/calculate?expression=7"))
    	assert(status(result) === OK)
    	assert(contentAsString(result) === "7.0")
    }
    
    it("Should respond with ACCESS_CONTROL_ALLOW_ORIGIN if ORIGIN is supplied") {
      val result = controllers.Application.calculate() {
          FakeRequest(GET, "/calculate?expression=7").withHeaders(ORIGIN -> "http://localhost")
      }
      assert(header(ACCESS_CONTROL_ALLOW_ORIGIN, result) === Some("http://localhost"))
    }
    
    it("Should not respond with ACCESS_CONTROL_ALLOW_ORIGIN if ORIGIN is not supplied") {
      val result = controllers.Application.calculate()(FakeRequest(GET, "/calculate?expression=7"))
      assert(header(ACCESS_CONTROL_ALLOW_ORIGIN, result) === None)
    }
  }
}