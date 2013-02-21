package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.data.Form
import play.api.data.Forms._
import models.Calculator
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import views.html.defaultpages.badRequest

object Application extends Controller {
  def index = Action {
    Ok(views.html.index())
  }

  val calculateForm = Form(
    "expression" -> text)

  def calculateAsyncAjax = Action {
    implicit request =>
      val expr = calculateForm.bindFromRequest.get
      val futureResult = Future {
        try {
          Calculator.apply(expr)
        } catch {
          case failure => {
            failure.getMessage
          }
        }
      }
      Async {
        futureResult.map(result => Ok("" + result))
      }
  }

  def calculateAjax = Action {
    implicit request =>
      val expr = calculateForm.bindFromRequest.get

      try {
        Ok("" + Calculator.apply(expr));
      } catch {
        case failure => {
          BadRequest(expr + " = " + failure.getMessage())
        }
      }
  }
}