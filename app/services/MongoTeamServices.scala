package services

import models._
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Document, MongoDatabase}

import javax.inject.Inject
import scala.concurrent.Future
import scala.util.Try


class MongoTeamServices @Inject()(myCompanyDatabase: MongoDatabase) extends AsyncTeamService {

  val teamCollection = myCompanyDatabase.getCollection("teams")

  override def create(team: Team): Unit = {
    val document: Document = teamToDocument(team)
    teamCollection
      .insertOne(document)
      .subscribe(
        r => println(s"Successful Insert $r"),
        t => t.printStackTrace(),
        () => "Insert Complete"
      )
  }


  override def findById(id: Long): Future[Option[Team]] = {
    teamCollection
      .find(equal("_id", id))
      .map { d =>
        documentToTeam(d)
      }
      .toSingle()
      .headOption()
  }

  private def teamToDocument(team: Team): Document = {
    Document(
      "_id" -> team.id,
      "name" -> team.name,
      "stadium" -> team.stadium.toString
    )
  }

  override def update(team: Team): Future[Try[Team]] = ???

  override def findAll(): Future[List[Team]] = teamCollection
    .find()
    .map(documentToTeam)
    .foldLeft(List.empty[Team])((list, team) => team :: list)
    .head()


  private def documentToTeam(d: Document) = {
    Team(
      d.getLong("_id"),
      d.getString("name"),
      Stadium(20L, d.getString("stadium"), "A City", "A Country", 100)
    )
  }

  override def findByName(name: String): Future[Option[Team]] = ???

  override def findByStadium(name: String): Future[List[Team]] = ???
}
