package services

import models.Stadium
import org.scalatestplus.play.PlaySpec

class MemoryStadiumServiceSpec extends PlaySpec{
  "MemoryStadiumService" must {
    "return a list stadium after I create a Stadium" in {
      val memoryStadiumService = new MemoryStadiumService()
      val stadium = Stadium(10L,"Elland Road", "Leeds", "UK", 2)
      memoryStadiumService.create(stadium)
      memoryStadiumService.findAll().size mustBe(1)
    }
    "be able to update a stadium from the service" in {
    val memoryStadiumService = new MemoryStadiumService();
    val stadium = Stadium(10L,"Elland Road", "Leeds", "UK", 2)
    memoryStadiumService.create(stadium)
    memoryStadiumService.findAll().size mustBe (1)
    val maybeStadium = memoryStadiumService.findById(10L).get
    val updated = stadium.copy(city = "Birmingham")
    println(updated)
    memoryStadiumService.update(updated)
    val result = memoryStadiumService.findById(10L).get
    result mustBe Stadium(10L,"Elland Road", "Birmingham", "UK", 2)
    memoryStadiumService.findAll().size mustBe (1)
  }
  }


}
