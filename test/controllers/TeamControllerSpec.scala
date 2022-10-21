package controllers

import com.dimafeng.testcontainers.{ForAllTestContainer, MongoDBContainer}
import org.mongodb.scala.MongoClient
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import services.{MongoTeamServices}

class TeamControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with ForAllTestContainer {
  val container: MongoDBContainer = new MongoDBContainer()

  "TeamController GET" should {
    "render the list of team in the /teams page" in {
      val controller = new TeamController(stubControllerComponents(), new MongoTeamServices(getDb))

      val teamsPage = controller.list().apply(FakeRequest(GET, "/teams"))

      status(teamsPage) mustBe OK
      contentType(teamsPage) mustBe Some("text/html")
      contentAsString(teamsPage) must include("to look at!")
    }

    "render the form to add a team in the /team page" in {
      val controller = new TeamController(stubControllerComponents(), new MongoTeamServices(getDb))
      val request = CSRFTokenHelper.addCSRFToken(FakeRequest(GET, "/team"))
      val teamFormPage = controller.init().apply(request)

      status(teamFormPage) mustBe OK
      contentType(teamFormPage) mustBe Some("text/html")
      contentAsString(teamFormPage) must include("Enter a team:")
    }
  }

  private def getDb = {
    val mongoClient: MongoClient =
      MongoClient(container.container.getConnectionString)
    val db = mongoClient.getDatabase("tests")
    db
  }
}
