package controllers
import models.Stadium

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.mvc._

import scala.util.hashing.MurmurHash3

case class StadiumData(name: String, city: String, country: String, seats: Int)

class StadiumController @Inject() (
  val controllerComponents: ControllerComponents
) extends BaseController {
  def list() = Action { implicit request =>
    val result = List(
      Stadium(10L, "Stamford Bridge", "A", "B", 1203),
      Stadium(12L, "Emirates Stadium", "A", "B", 1001),
      Stadium(13L, "Ashburton Grove", "A", "B", 2000),
      Stadium(15L, "The Dripping Pan", "A", "B", 4000),
    )
    Ok(views.html.stadium.stadiums(result))
  }

  val stadiumForm = Form(
    mapping(
      "name" -> text,
      "city" -> text,
      "country" -> text,
      "seats" -> number
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
            id, stadiumData.name, stadiumData.city, stadiumData.country, stadiumData.seats
          )
          println("Yay!" + newUser)
          Redirect(routes.StadiumController.show(id))
        }
      )
  }

  def show(id: Long) = Action { implicit request =>
    Ok("This is a placeholder for this stadium")
  }
}

