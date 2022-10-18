import com.google.inject.{AbstractModule, Provides}
import org.mongodb.scala.{MongoClient, MongoDatabase}
import play.api.Configuration
import services.{AsyncStadiumService, MemoryPlayerService, MemoryStadiumService, MongoStadiumServices, PlayerService, StadiumService}

class Module extends AbstractModule{
  override def configure(): Unit = {

    @Provides
    def databaseProvider(configuration: Configuration): MongoDatabase = {
      val username = configuration.get[String]("mongo.username")
      val password = configuration.get[String]("mongo.password")
      val database = configuration.get[String]("mongo.database")
      val url = configuration.get[String]("mongo.host")
      val port = configuration.get[String]("mongo.port")
      val mongoClient: MongoClient = MongoClient(
        s"mongodb://$username:$password@localhost:" + 27017
      )
      mongoClient.getDatabase(database)
    }

    bind(classOf[StadiumService]).to(classOf[MemoryStadiumService]).in(classOf[javax.inject.Singleton])
    bind(classOf[PlayerService]).to(classOf[MemoryPlayerService]).in(classOf[javax.inject.Singleton])
    bind(classOf[AsyncStadiumService]).to(classOf[MongoStadiumServices])
  }
}
