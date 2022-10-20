package models

import org.scalatestplus.play.PlaySpec

class StadiumSpec extends PlaySpec {
  val stadium = Stadium(12L, "Anfield", "Liverpool", "UK", 100000)
  "A stadium" must {
    "return the name that I provide it" in {
      stadium.name mustBe ("Anfield")
    }
    "return the city that I provide it" in {
      stadium.city mustBe ("Liverpool")
    }
    "return the country that I provide it" in {
      stadium.country mustBe ("UK")
    }
    "return the number of seats that I provide it" in {
      stadium.seats mustBe (100000)
    }
  }
}
