package services

import models.Stadium
import org.mongodb.scala.connection.ClusterSettings
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Document, MongoClient, MongoClientSettings, MongoCredential, MongoDatabase, ServerAddress}

import javax.inject.Inject
import scala.concurrent.Future
import scala.util.Try

class MongoStadiumServices @Inject()(myCompanyDatabase: MongoDatabase) extends AsyncStadiumService {

  val stadiumCollection = myCompanyDatabase.getCollection("stadiums")

  override def create(stadium: Stadium): Unit = {
    val document: Document = stadiumToDocument(stadium)
    stadiumCollection
      .insertOne(document)
      .subscribe(
        r => println(s"Successful Insert $r"),
        t => t.printStackTrace(),
        () => "Insert Complete"
      )
  }


  override def findById(id: Long): Future[Option[Stadium]] = {
    stadiumCollection
      .find(equal("_id", id))
      .map { d =>
        documentToStadium(d)
      }
      .toSingle()
      .headOption()
  }

  private def stadiumToDocument(stadium: Stadium) = {
    Document(
      "_id" -> stadium.id,
      "name" -> stadium.name,
      "seats" -> stadium.seats,
      "city" -> stadium.city,
      "country" -> stadium.country
    )
  }

  override def update(stadium: Stadium): Future[Try[Stadium]] = ???

  override def findAll(): Future[List[Stadium]] = stadiumCollection
    .find()
    .map(documentToStadium)
    .foldLeft(List.empty[Stadium])((list, stadium) => stadium :: list)
    .head()

  private def documentToStadium(d: Document) = {
    Stadium(
      d.getLong("_id"),
      d.getString("name"),
      d.getString("city"),
      d.getString("country"),
      d.getInteger("seats")
    )
  }
  override def findByName(name: String): Future[Option[Stadium]] = ???

  override def findByCountry(name: String): Future[List[Stadium]] = ???
}
