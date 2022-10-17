package controllers

import models._
import play.api.mvc.{BaseController, ControllerComponents}
import services.{MemoryPlayerService, PlayerService}
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.data.validation.Constraints.nonEmpty
import play.api.mvc._

import javax.inject._
import scala.util.hashing.MurmurHash3

case class PlayerData(team: String, position: String, firstName: String, lastName: String)

class PlayerController @Inject() (
  val controllerComponents: ControllerComponents,
  val playerService: PlayerService
  ) extends BaseController with play.api.i18n.I18nSupport {
  def list() = Action { implicit request =>
    val result = playerService.findAll()
    Ok(views.html.player.players(result))
  }

  val playerForm = Form(
    mapping(
      "team" -> text.verifying(nonEmpty),
      "position" -> text.verifying(nonEmpty),
      "firstName" -> text.verifying(nonEmpty),
      "lastName" -> text.verifying(nonEmpty),
    )(PlayerData.apply)
    (PlayerData.unapply)
  )

  def init(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.player.create(playerForm))
  }

  def create() = Action { implicit request =>
    playerForm.bindFromRequest().fold(
      formWithErrors => {
        println("Nay!" + formWithErrors)
        BadRequest(views.html.player.create(formWithErrors))
      },
      playersData => {
        val id = MurmurHash3.stringHash(playersData.team)
        val newPlayers = models.Player(
          id,
          Team(10L, playersData.team, Stadium("House of Jacques")),
          playersData.position match {
            case "GoalKeeper" => GoalKeeper
            case "RightFullback" => RightFullback
            case "LeftFullback" => LeftFullback
            case "CenterBack" => CenterBack
            case "Sweeper" => Sweeper
            case "Striker" => Striker
            case "HoldingMidfielder" => HoldingMidfielder
            case "RightMidfielder" => RightMidfielder
            case "Central" => Central
            case "AttackingMidfielder" => AttackingMidfielder
            case "LeftMidfielder" => LeftMidfielder
            case _ => Referee
          },
          playersData.firstName,
          playersData.lastName
        )
        println("Yay!" + newPlayers)
        playerService.create(newPlayers)
        Redirect(routes.PlayerController.show(id))
      }
    )

  }

  def show(id: Long) = Action { implicit request =>
    val maybePlayers = playerService.findById(id)
    maybePlayers
      .map(s => Ok(views.html.player.show(s)))
      .getOrElse(NotFound("Sorry, that player is not found"))
  }
}