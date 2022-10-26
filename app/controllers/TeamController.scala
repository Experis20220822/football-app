package controllers

import models.Team
import play.api._
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, text}
import play.api.data.validation.Constraints.nonEmpty
import play.api.mvc._
import services.{AsyncStadiumService, AsyncTeamService}

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.hashing.MurmurHash3

case class TeamData(name: String, stadiumId: Long)

class TeamController @Inject() (
  val controllerComponents: ControllerComponents,
  val teamService: AsyncTeamService,
  val stadiumService: AsyncStadiumService
  ) extends BaseController with play.api.i18n.I18nSupport {
  def list() = Action.async { implicit request =>
    teamService.findAll().map(xs => Ok(views.html.team.teams(xs)))
  }

  val teamForm = Form(
    mapping(
      "name" -> text.verifying(nonEmpty),
      "stadiumId" -> longNumber
    )(TeamData.apply)
    (TeamData.unapply)
  )

  def init(): Action[AnyContent] = Action.async { implicit request =>
    stadiumService.findAll()
      .map(xs => Ok(views.html.team.create(teamForm, xs)))
  }

  def create() = Action.async {
    implicit request =>
      println(request.body)
      teamForm.bindFromRequest().fold(
        formWithErrors => {
          println("Nay!" + formWithErrors)
          stadiumService.findAll()
            .map(xs => Ok(views.html.team.create(formWithErrors, xs)))
        },
        teamData => {
          val eventualMaybeStadium = stadiumService.findById(teamData.stadiumId)
          val id = MurmurHash3.stringHash(teamData.name)
          eventualMaybeStadium.map { maybeStadium =>
            maybeStadium.map { stadium =>
              val team = Team(
                id,
                teamData.name,
                stadium,
                stadium.id
              )
              teamService.create(team)
              Redirect(routes.TeamController.show(id))
            }.getOrElse(NotFound)
          }
        }
      )
  }

  def show(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val maybeTeam = teamService.findById(id)
    maybeTeam
      .map {
        case Some(teamStadiumView) => Ok(views.html.team.show(teamStadiumView))
        case None => NotFound("Sorry, that team must be rubbish - not found")
      }
  }

}
