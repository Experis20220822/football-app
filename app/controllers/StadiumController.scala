package controllers
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.data.validation.Constraints.{max, min, nonEmpty}
import play.api.mvc._
import services.StadiumService

import javax.inject._
import scala.util.hashing.MurmurHash3

case class StadiumData(name: String, city: String, country: String, seats: Int)

class StadiumController @Inject() (
  val controllerComponents: ControllerComponents,
  val stadiumService: StadiumService
) extends BaseController
    with play.api.i18n.I18nSupport {
  def list() = Action { implicit request =>
    val result = stadiumService.findAll()
    Ok(views.html.stadium.stadiums(result))
  }

  val stadiumForm = Form(
    mapping(
      "name" -> text.verifying(nonEmpty),
      "city" -> text.verifying(nonEmpty),
      "country" -> text.verifying(nonEmpty),
      "seats" -> number.verifying(min(0), max(100))
    )(StadiumData.apply)
    (StadiumData.unapply)
  )

  def init(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.stadium.create(stadiumForm))
  }

  def create() = Action {
    implicit request =>
      stadiumForm.bindFromRequest.fold(
        formWithErrors => {
          println("Nay!" + formWithErrors)
          BadRequest(views.html.stadium.create(formWithErrors))
        },
        stadiumData => {
          val id = MurmurHash3.stringHash(stadiumData.name)
          val newUser = models.Stadium(
            id,
            stadiumData.name,
            stadiumData.city,
            stadiumData.country,
            stadiumData.seats
          )
          println("Yay!" + newUser)
          stadiumService.create(newUser)
          Redirect(routes.StadiumController.show(id))
        }
      )
  }

  def show(id: Long): Action[AnyContent] = Action.apply { implicit request =>
    val maybeStadium = stadiumService.findById(id)
    maybeStadium
      .map(s => Ok(views.html.stadium.show(s)))
      .getOrElse(NotFound("Sorry, that stadium no existo"))
  }
}

