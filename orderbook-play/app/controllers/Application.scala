package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Application extends Controller {

  val hitForm = Form(
    tuple(
      "quantity" -> number,
      "price" -> number))

  def index = Action {
    Ok(views.html.index())
  }

}
