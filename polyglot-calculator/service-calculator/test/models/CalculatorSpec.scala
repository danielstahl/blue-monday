package models

import org.scalatest.FunSpec

class CalculatorSpec extends FunSpec {
	describe("A calculator") {
	  it("should calculate 2 + 1 = 3") {
	    assert(Calculator.apply("2 + 1") ===3.0)
	  }
	  it("should calculate 3 - 1 = 2") {
	    assert(Calculator.apply("3 - 1")  ===2.0)
	  }
	  it("should calculate 3 * 2 = 6") {
	    assert(Calculator.apply("3 * 2") === 6.0)
	  }
	  it("should calculate 6 / 2 = 3") {
	    assert(Calculator.apply("6 / 2") === 3.0)
	  }
	}
}