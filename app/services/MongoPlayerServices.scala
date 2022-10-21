package services

import models._
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Document, MongoDatabase}
import javax.inject.Inject
import scala.concurrent.Future
import scala.util.Try


class MongoPlayerServices @Inject()(myCompanyDatabase: MongoDatabase) extends AsyncPlayerService {

  val playerCollection = myCompanyDatabase.getCollection("players")

  override def create(player: Player): Unit = {
    val document: Document = playerToDocument(player)
    playerCollection
      .insertOne(document)
      .subscribe(
        r => println(s"Successful Insert $r"),
        t => t.printStackTrace(),
        () => "Insert Complete"
      )
  }


  override def findById(id: Long): Future[Option[Player]] = {
    playerCollection
      .find(equal("_id", id))
      .map { d =>
        documentToPlayer(d)
      }
      .toSingle()
      .headOption()
  }

  private def playerToDocument(player: Player): Document = {
    Document(
      "_id" -> player.id,
      "team" -> player.teamId,
      "position" -> player.position.toString,
      "firstName" -> player.firstName,
      "lastName" -> player.lastName,
    )
  }

  override def update(player: Player): Future[Try[Player]] = ???

  override def findAll(): Future[List[Player]] = playerCollection
    .find()
    .map(documentToPlayer)
    .foldLeft(List.empty[Player])((list, player) => player :: list)
    .head()

  private def stringToPosition(string: String): Position = {
    string match {
      case "GoalKeeper" => GoalKeeper
      case "RightFullback" => RightFullback
      case "LeftFullback" => LeftFullback
      case "CenterBack" => CenterBack
      case "Sweeper" => Sweeper
      case "Striker" => Striker
      case "HoldingMidfielder" => HoldingMidfielder
      case "RightMidfielder" => RightMidfielder
      case "Central" => Central
      case "AttackingMidfielder" => AttackingMidfielder
      case "LeftMidfielder" => LeftMidfielder
      case _ => GoalKeeper
    }
  }

  private def documentToPlayer(d: Document) = {
    Player(
      d.getLong("_id"),
      d.getLong("teamId"),
      stringToPosition(d.getString("position")),
      d.getString("firstName"),
      d.getString("lastName"),
    )
  }

  override def findByName(name: String): Future[Option[Player]] = ???

  override def findByCountry(name: String): Future[List[Player]] = ???
}
