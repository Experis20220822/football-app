package services

import models.{Player, Position, Team}

import scala.util.Try

trait PlayerService {

  def create(player: Player): Unit

  def update(player: Player): Try[Player]

  def findById(id: Long): Option[Player]

  def findAll(): List[Player]

  def findByFirstName(firstName: String): List[Player]

  def findByLastName(lastName: String): List[Player]

  def findByPosition(position: Position): List[Player]

}
