package services

import models.Player

import scala.concurrent.Future
import scala.util.Try

trait AsyncPlayerService {
  def findById(id: Long): Future[Option[Player]]

  def create(player: Player): Unit

  def update(player: Player): Future[Try[Player]]

  def findAll(): Future[List[Player]]

  def findByName(name: String): Future[Option[Player]]

  def findByCountry(name: String): Future[List[Player]]
}
