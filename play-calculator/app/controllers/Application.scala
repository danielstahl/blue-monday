package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.data.Form
import play.api.data.Forms._
import models.Calculator
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import views.html.defaultpages.badRequest
import scala.util.Success
import scala.util.Failure

object Application extends Controller {
  def index = Action {
    Ok(views.html.index())
  }

  val calculateForm = Form(
    "expression" -> text
  )

  def calculateAsyncAjax = Action {
    implicit request =>
      val expression = calculateForm.bindFromRequest.get
      val futureResult: Future[Double] = Future { Calculator.apply(expression)}
      
      Async {
        futureResult map {
          okResult => Ok("" + okResult)
        } recover {
          case failure => BadRequest(failure.getMessage())
        }
      }
  }

  def calculateAjax = Action {
    implicit request =>
      val expr = calculateForm.bindFromRequest.get

      try {
        Ok("" + Calculator.apply(expr));
      } catch {
        case failure => {
          BadRequest(failure.getMessage())
        }
      }
  }
}