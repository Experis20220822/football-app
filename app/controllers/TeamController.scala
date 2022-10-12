package controllers
import models.Team
import models.Stadium

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.mvc._

import scala.util.hashing.MurmurHash3

case class TeamData(name: String, stadium: String)

class TeamController @Inject() (
  val controllerComponents: ControllerComponents
  ) extends BaseController {
  def list() = Action { implicit request =>
    val result = List(
      Team(10L, "Chelsea", Stadium("Stamford Bridge")),
      Team(10L, "Arsenal", Stadium("Emirates Stadium")),
      Team(10L, "Lewes FC", Stadium("The Dripping Pan")),
    )
    Ok(views.html.team.teams(result))
  }

  val teamForm = Form(
    mapping(
      "name" -> text,
      "stadium" -> text,
    )(TeamData.apply)
    (TeamData.unapply)
  )

  def init(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.team.create(teamForm))
  }

  def create() = Action {
    implicit request =>
      println(request.body)
      teamForm.bindFromRequest.fold(
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
          Redirect(routes.TeamController.show(id))
        }
      )
  }

  def show(id: Long) = Action { implicit request =>
    Ok("This is a placeholder for this team")
  }

}
