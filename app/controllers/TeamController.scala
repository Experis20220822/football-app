package controllers

import models.Stadium
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.data.validation.Constraints.nonEmpty
import play.api.mvc._
import services.AsyncTeamService

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.hashing.MurmurHash3

case class TeamData(name: String, stadium: String)

class TeamController @Inject() (
  val controllerComponents: ControllerComponents,
  val teamService: AsyncTeamService
  ) extends BaseController with play.api.i18n.I18nSupport {
  def list() = Action.async { implicit request =>
    teamService.findAll().map(xs => Ok(views.html.team.teams(xs)))
  }

  val teamForm = Form(
    mapping(
      "name" -> text.verifying(nonEmpty),
      "stadium" -> text.verifying(nonEmpty),
    )(TeamData.apply)
    (TeamData.unapply)
  )

  def init(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.team.create(teamForm))
  }

  def create() = Action {
    implicit request =>
      println(request.body)
      teamForm.bindFromRequest().fold(
        formWithErrors => {
          println("Nay!" + formWithErrors)
          BadRequest(views.html.team.create(formWithErrors))
        },
        teamData => {
          val id = MurmurHash3.stringHash(teamData.name)
          val newUser = models.Team(
            id, teamData.name, Stadium(teamData.stadium)
          )
          println("Yay!" + newUser)
          teamService.create(newUser)
          Redirect(routes.TeamController.show(id))
        }
      )
  }

  def show(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val maybeTeam = teamService.findById(id)
    maybeTeam
      .map {
        case Some(team) => Ok(views.html.team.show(team))
        case None => NotFound("Sorry, that team must be rubbish - not found")
      }
  }

}
