package models

import org.scalatestplus.play.PlaySpec

class TeamSpec extends PlaySpec {
  val team = Team(12L, "Leeds", Stadium(12L, "Elland Road","Leeds", "UK", 6))
  "A team" must {
    "return the name that I provide it" in {
      team.name mustBe ("Leeds")
    }
    "return the stadium that I provide it" in {
      team.stadium mustBe (Stadium(12L, "Elland Road", "Leeds", "UK", 6))
    }
  }
}
