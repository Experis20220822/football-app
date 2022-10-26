package services

import models._
import org.mongodb.scala.model.{Aggregates, Filters}
import org.mongodb.scala.{Document, MongoDatabase}

import javax.inject.Inject
import scala.concurrent.Future
import scala.jdk.CollectionConverters._
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


  override def findById(id: Long): Future[Option[TeamStadiumView]] = {
    val aggregated = teamCollection.aggregate(
      Seq(
        Aggregates.`match`(Filters.equal("_id", id)),
        Aggregates
          .lookup("stadium", "stadium", "_id", "stadiumArray")
      )
    )

    aggregated
      .map(d => {
        val stadiumName = d
          .getList("stadiumArray", classOf[java.util.Map[_, _]])
          .asScala.head.get("name")
        TeamStadiumView(
          d.getLong("_id"),
          d.getString("name"),
          stadiumName.asInstanceOf[String],
          d.getLong("stadiumId")
        )
      }).toSingle().headOption()
  }

  private def teamToDocument(team: Team): Document = {
    Document(
      "_id" -> team.id,
      "name" -> team.name,
      "stadium" -> team.stadiumId
    )
  }

  override def update(team: Team): Future[Try[Team]] = ???

  override def findAll(): Future[List[TeamStadiumView]] = {
    val aggregated = teamCollection.aggregate(
      Seq(
        Aggregates
          .lookup("stadium", "stadium", "_id", "stadiumArray")
      )
    )

    aggregated
      .map(d => {
        val stadiumName = d
          .getList("stadiumArray", classOf[java.util.Map[_, _]])
          .asScala.head.get("name")
        TeamStadiumView(
          d.getLong("_id"),
          d.getString("name"),
          stadiumName.asInstanceOf[String],
          d.getLong("stadium")
        )
      }).foldLeft(List.empty[TeamStadiumView])((list, team) => team :: list)
      .head()
  }

  private def documentToTeam(d: Document) = {
    Team(
      d.getLong("_id"),
      d.getString("name"),
      Stadium(20L, d.getString("stadium"), "A City", "A Country", 100),
      d.getLong("stadiumId")
    )
  }

  override def findByName(name: String): Future[Option[Team]] = ???

  override def findByStadium(name: String): Future[List[Team]] = ???
}
