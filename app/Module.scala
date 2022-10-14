import com.google.inject.AbstractModule
import services.{
  AsyncStadiumService,
  MemoryStadiumService,
  MongoStadiumServices,
  StadiumService}

class Module extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[StadiumService]).to(classOf[MemoryStadiumService]).in(classOf[javax.inject.Singleton])
    bind(classOf[AsyncStadiumService]).to(classOf[MongoStadiumServices])
  }
}
