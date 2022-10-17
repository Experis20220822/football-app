import com.google.inject.AbstractModule
import services.{AsyncStadiumService, MemoryPlayerService, MemoryStadiumService, MongoStadiumServices, PlayerService, StadiumService}

class Module extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[StadiumService]).to(classOf[MemoryStadiumService]).in(classOf[javax.inject.Singleton])
    bind(classOf[PlayerService]).to(classOf[MemoryPlayerService]).in(classOf[javax.inject.Singleton])
    bind(classOf[AsyncStadiumService]).to(classOf[MongoStadiumServices])
  }
}
