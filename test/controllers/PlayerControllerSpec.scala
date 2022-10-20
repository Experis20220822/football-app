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

      val stadiumsPage = controller.list().apply(FakeRequest(GET, "/players"))

      status(stadiumsPage) mustBe OK
      contentType(stadiumsPage) mustBe Some("text/html")
      contentAsString(stadiumsPage) must include("to have a goosey at!")
    }

    "render the form to add a player in the /player page" in {
      val controller = new PlayerController(stubControllerComponents(), new MongoPlayerServices(getDb), new MongoTeamServices(getDb))
      val request = CSRFTokenHelper.addCSRFToken(FakeRequest(GET, "/player"))
      val stadiumFormPage = controller.init().apply(request)

      status(stadiumFormPage) mustBe OK
      contentType(stadiumFormPage) mustBe Some("text/html")
      contentAsString(stadiumFormPage) must include("Enter a player:")
    }
  }

  private def getDb = {
    val mongoClient: MongoClient =
      MongoClient(container.container.getConnectionString)
    val db = mongoClient.getDatabase("tests")
    db
  }
}
