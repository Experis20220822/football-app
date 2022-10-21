package controllers

import models._
import play.api.mvc.{BaseController, ControllerComponents}
import services.{AsyncPlayerService, AsyncTeamService, MemoryPlayerService, PlayerService}
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, text}
import play.api.data.validation.Constraints.nonEmpty
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.hashing.MurmurHash3

case class PlayerData(teamId: Long, position: String, firstName: String, lastName: String)

class PlayerController @Inject() (
  val controllerComponents: ControllerComponents,
  val playerService: AsyncPlayerService,
  val teamService: AsyncTeamService
  ) extends BaseController with play.api.i18n.I18nSupport {
  def list() = Action.async { implicit request =>
    playerService.findAll().map(p => Ok(views.html.player.players(p))
    )
  }

  val playerForm: Form[PlayerData] = Form(
    mapping(
      "team" -> longNumber,
      "position" -> text.verifying(nonEmpty),
      "firstName" -> text.verifying(nonEmpty),
      "lastName" -> text.verifying(nonEmpty),
    )(PlayerData.apply)
    (PlayerData.unapply)
  )

  def init(): Action[AnyContent] = Action.async { implicit request =>
    teamService
      .findAll()
      .map(xs => Ok(views.html.player.create(playerForm, xs)))
  }

  def create() = Action.async { implicit request =>
    playerForm.bindFromRequest().fold(
      formWithErrors => {
        println("Nay!" + formWithErrors)
        teamService.findAll()
        .map(xs => BadRequest(views.html.player.create(formWithErrors, xs)))
      },
      playersData => {
        val maybeTeam = teamService.findById(playersData.teamId)
        val id = MurmurHash3.stringHash(playersData.firstName)
        maybeTeam
          .map { t =>
            Player(
              id,
              t match {
                case Some(t) => t.id
              },
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
          }
          .map { p =>
            playerService.create(p)
            Redirect(routes.PlayerController.show(id))
          }
      }
    )

  }

  def show(id: Long) = Action.async { implicit request =>
    val maybePlayers = playerService.findById(id)
    maybePlayers
      .map {
        case Some(player) => Ok(views.html.player.show(player))
        case None => NotFound("Sorry, that player is not found")
      }
  }
}