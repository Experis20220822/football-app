//package controllers
//
//import models._
//import play.api.mvc.{BaseController, ControllerComponents}
//import services.{MemoryPlayerService, PlayerService}
//import play.api._
//import play.api.data.Form
//import play.api.data.Forms.{mapping, text}
//import play.api.data.validation.Constraints.nonEmpty
//import play.api.mvc._
//
//import javax.inject._
//import scala.util.hashing.MurmurHash3
//
//case class PlayerData(team: String, position: String, firstName: String, lastNAme: String)
//
//class PlayerController @Inject() (
//  val controllerComponents: ControllerComponents,
//  val playerService: PlayerService
//  ) extends BaseController with play.api.i18n.I18nSupport {
//  def list() = Action { implicit request =>
//    val result = playerService.findAll()
//    Ok(views.html.players(result))
//  }
//
//  val playerForm = Form(
//    mapping(
//      "name" -> text,
//      "lastName" -> text,
//      "team" -> text,
//      "position" -> text
//    )(PlayerData.apply)
//    (PlayerData.unapply)
//  )
//
//  def init(): Action[AnyContent] = Action { implicit request =>
//    Ok(views.html.players.create(playerForm))
//  }
//
//  def create() = Action { implicit request =>
//    playerForm.bindFromRequest.fold(
//      formWithErrors => {
//        println("Nay!" + formWithErrors)
//        BadRequest(views.html.players.create(formWithErrors))
//      },
//      playersData => {
//        val id = MurmurHash3.stringHash(playersData.team)
//        val newPlayers = models.Player(
//          id,
//          playersData.team,
//          playersData.position,
//          playersData.firstName,
//          playersData.lastNAme
//        )
//        println("Yay!" + newPlayers)
//        playerService.create(newPlayers)
//        Redirect(routes.PlayerController.show(id))
//      }
//    )
//
//  }
//
//  def show(id: Long) = Action { implicit request =>
//    val maybePlayers = playerService.findById(id)
//    maybePlayers
//      .map(s => Ok(views.html.players.show(s)))
//      .getOrElse(NotFound("Sorry, that player is not found"))
//  }
//}