package services
import com.dimafeng.testcontainers.{ForAllTestContainer, MongoDBContainer}
import models.{Stadium, Team}
import org.mongodb.scala._
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global

class MongoTeamServiceSpec extends PlaySpec with ForAllTestContainer {
  val container: MongoDBContainer = new MongoDBContainer()
  val host: String = container.host

  "Mongo Team Service" must {
    "create a team document" in {
      val teamService = new MongoTeamServices(getDb)
      val team = Team(12L, "Charlton", Stadium(10L, "The Valley", "London", "UK", 27000))
      teamService.create(team)

      val result = teamService.findById(12L).map {
        case Some(s) => s
        case _ => ()
      }
      result.map(s => s mustEqual team)
    }

    "find a team by it's id" in {
      val teamService = new MongoTeamServices(getDb)
      val team = Team(12L, "Charlton", Stadium(10L, "The Valley", "London", "UK", 27000))
      teamService.create(team)
      teamService.findById(12L).map(s => s mustEqual Document(
        "_id" -> 12L,
        "name" -> "Charlton",
        "stadium" -> "Stadium(10L, The Valley, London, UK, 27000)"
      ))
    }

    "list all teams" in {
      val teamService = new MongoTeamServices(getDb)
      val team = Team(12L, "Charlton", Stadium(10L, "The Valley", "London", "UK", 27000))
      teamService.create(team)
      teamService.findAll().map(s => s.size mustEqual 1)
    }
  }

  private def getDb = {
    val mongoClient: MongoClient =
      MongoClient(container.container.getConnectionString)
    val db = mongoClient.getDatabase("tests")
    db
  }
}
