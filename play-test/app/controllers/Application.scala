package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.data.Form
import play.api.data.Forms._
import models.Calculator
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

object Application extends Controller {
  def index = Action {
    Ok(views.html.index(calculateForm, "-"))
  }

  val calculateForm = Form(
    "expression" -> text
  )

  def calculate = Action {
    implicit request =>
      val expr = calculateForm.bindFromRequest.get
      val result =
        try {
          Calculator.apply(expr)
        } catch {
          case failure => {
            failure.getMessage
          }
        }
       Ok(views.html.index(calculateForm, expr + " = " + result))
  }

  def calculateAsync = Action {
    implicit request =>
      val expr = calculateForm.bindFromRequest.get
      val futureResult = Future {
        Calculator.apply(expr)
      }
      Async {
        futureResult.map( result => Ok(views.html.index(calculateForm, expr + " = " + result)))
      }
  }
}