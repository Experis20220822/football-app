package controllers

import com.dimafeng.testcontainers.{ForAllTestContainer, MongoDBContainer}
import org.mongodb.scala.MongoClient
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import services.{MongoStadiumServices}

class StadiumControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with ForAllTestContainer {
  val container: MongoDBContainer = new MongoDBContainer()

  "StadiumController GET" should {
    "render the list of stadium in the /stadiums page" in {
      val controller = new StadiumController(stubControllerComponents(), new MongoStadiumServices(getDb))

      val stadiumsPage = controller.list().apply(FakeRequest(GET, "/stadiums"))

      status(stadiumsPage) mustBe OK
      contentType(stadiumsPage) mustBe Some("text/html")
      contentAsString(stadiumsPage) must include("to look at!")
    }

    "render the form to add a stadium in the /stadium page" in {
      val controller = new StadiumController(stubControllerComponents(), new MongoStadiumServices(getDb))
      val request = CSRFTokenHelper.addCSRFToken(FakeRequest(GET, "/stadium"))
      val stadiumFormPage = controller.init().apply(request)

      status(stadiumFormPage) mustBe OK
      contentType(stadiumFormPage) mustBe Some("text/html")
      contentAsString(stadiumFormPage) must include("Enter a stadium:")
    }
  }

  private def getDb = {
    val mongoClient: MongoClient =
      MongoClient(container.container.getConnectionString)
    val db = mongoClient.getDatabase("tests")
    db
  }
}
