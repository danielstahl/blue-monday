package models

import org.specs2.mutable.Specification


class CalculatorSpec extends Specification {
	"The calculator" should {
	  "calculate 2 + 1 = 3" in {
	    Calculator.apply("2 + 1") must beEqualTo(3.0)
	  }
	  "calculate 3 - 1 = 2" in {
	    Calculator.apply("3 - 1") must beEqualTo(2.0)
	  }
	  "calculate 3 * 2 = 6" in {
	    Calculator.apply("3 * 2") must beEqualTo(6.0)
	  }
	  "calculate 6 / 2 = 3" in {
	    Calculator.apply("6 / 2") must beEqualTo(3.0)
	  }
	}
}