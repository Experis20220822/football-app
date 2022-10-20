package models

import org.scalatestplus.play.PlaySpec

class PlayerSpec extends PlaySpec {
  val player = Player(12L, 10L, Striker, "Romel", "Williams")
  "A stadium" must {
    "return the teamId that I provide it" in {
      player.teamId mustBe (10L)
    }
    "return the position that I provide it" in {
      player.position mustBe (Striker)
    }
    "return the first name that I provide it" in {
      player.firstName mustBe ("Romel")
    }
    "return the last name that I provide it" in {
      player.lastName mustBe ("Williams")
    }
  }
}
