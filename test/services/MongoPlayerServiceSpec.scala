package services
import com.dimafeng.testcontainers.{ForAllTestContainer, MongoDBContainer}
import models.{Player, Stadium, Striker}
import org.mongodb.scala._
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global

class MongoPlayerServiceSpec extends PlaySpec with ForAllTestContainer {
  val container: MongoDBContainer = new MongoDBContainer()
  val host: String = container.host

  "Mongo Player Service" must {
    "create a player document" in {
      val playerService = new MongoPlayerServices(getDb)
      val player = Player(14L, 12L, Striker, "Lee", "Powell")
      playerService.create(player)

      val result = playerService.findById(14L).map {
        case Some(s) => s
        case _ => ()
      }
      result.map(s => s mustEqual player)
    }

    "find a player by it's id" in {
      val playerService = new MongoPlayerServices(getDb)
      val player = Player(14L, 12L, Striker, "Lee", "Powell")
      playerService.create(player)
      playerService.findById(14L).map(s => s mustEqual Document(
        "_id" -> 14L,
        "team" -> 12L,
        "position" -> "Striker",
        "firstName" -> "Lee",
        "lastName" -> "Powell"
      ))
    }

    "list all players" in {
      val playerService = new MongoPlayerServices(getDb)
      val player = Player(14L, 12L, Striker, "Lee", "Powell")
      playerService.create(player)
      playerService.findAll().map(s => s.size mustEqual 1)
    }
  }

  private def getDb = {
    val mongoClient: MongoClient =
      MongoClient(container.container.getConnectionString)
    val db = mongoClient.getDatabase("tests")
    db
  }
}
