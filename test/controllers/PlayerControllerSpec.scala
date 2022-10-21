package controllers

import com.dimafeng.testcontainers.{ForAllTestContainer, MongoDBContainer}
import org.mongodb.scala.MongoClient
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import services.{MongoPlayerServices, MongoTeamServices}

class PlayerControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with ForAllTestContainer {
  val container: MongoDBContainer = new MongoDBContainer()

  "PlayerController GET" should {
    "render the list of player in the /players page" in {
      val controller = new PlayerController(stubControllerComponents(), new MongoPlayerServices(getDb), new MongoTeamServices(getDb))

      val playersPage = controller.list().apply(FakeRequest(GET, "/players"))

      status(playersPage) mustBe OK
      contentType(playersPage) mustBe Some("text/html")
      contentAsString(playersPage) must include("to have a goosey at!")
    }

    "render the form to add a player in the /player page" in {
      val controller = new PlayerController(stubControllerComponents(), new MongoPlayerServices(getDb), new MongoTeamServices(getDb))
      val request = CSRFTokenHelper.addCSRFToken(FakeRequest(GET, "/player"))
      val playerFormPage = controller.init().apply(request)

      status(playerFormPage) mustBe OK
      contentType(playerFormPage) mustBe Some("text/html")
      contentAsString(playerFormPage) must include("Enter a player:")
    }
  }

  private def getDb = {
    val mongoClient: MongoClient =
      MongoClient(container.container.getConnectionString)
    val db = mongoClient.getDatabase("tests")
    db
  }
}
