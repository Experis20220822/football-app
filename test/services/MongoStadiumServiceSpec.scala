package services
import com.dimafeng.testcontainers.{ForAllTestContainer, MongoDBContainer}
import models.{Stadium}
import org.mongodb.scala._
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global

class MongoStadiumServiceSpec extends PlaySpec with ForAllTestContainer {
  val container: MongoDBContainer = new MongoDBContainer()
  val host: String = container.host

  "Mongo Stadium Service" must {
    "create a stadium document" in {
      val stadiumService = new MongoStadiumServices(getDb)
      val stadium = Stadium(10L, "The Valley", "London", "UK", 27000)
      stadiumService.create(stadium)

      val result = stadiumService.findById(10L).map {
        case Some(s) => s
        case _ => ()
      }
      result.map(s => s mustEqual stadium)
    }

    "find a stadium by it's id" in {
      val stadiumService = new MongoStadiumServices(getDb)
      val stadium = Stadium(10L, "The Valley", "London", "UK", 27000)
      stadiumService.create(stadium)
      stadiumService.findById(10L).map(s => s mustEqual Document(
        "_id" -> 10L,
        "name" -> "The Valley",
        "city" -> "London",
        "country" -> "UK",
        "seats" -> 27000
      ))
    }

    "list all stadiums" in {
      val stadiumService = new MongoStadiumServices(getDb)
      val stadium = Stadium(10L, "The Valley", "London", "UK", 27000)
      stadiumService.create(stadium)
      stadiumService.findAll().map(s => s.size mustEqual 1)
    }
  }

  private def getDb = {
    val mongoClient: MongoClient =
      MongoClient(container.container.getConnectionString)
    val db = mongoClient.getDatabase("tests")
    db
  }
}
