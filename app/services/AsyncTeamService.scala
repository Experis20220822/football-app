package services

import models.Team

import scala.concurrent.Future
import scala.util.Try

trait AsyncTeamService {
  def findById(id: Long): Future[Option[TeamStadiumView]]

  def create(player: Team): Unit

  def update(player: Team): Future[Try[Team]]

  def findAll(): Future[List[TeamStadiumView]]

  def findByName(name: String): Future[Option[Team]]

  def findByStadium(name: String): Future[List[Team]]
}
