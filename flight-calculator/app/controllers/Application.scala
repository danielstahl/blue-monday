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
import models.CalculatorActor
import akka.pattern.ask
import play.api.libs.concurrent.Execution.Implicits._
import akka.util.Timeout
import scala.concurrent.duration._

object Application extends Controller {
  def index = Action {
    Ok(views.html.index())
  }

  val calculateForm = Form(
    "expression" -> text)

  def calculate() = Action {
    implicit request =>
      val expression = calculateForm.bindFromRequest.get
      val futureResult: Future[Double] = Future { Calculator.apply(expression) }
      Async {
        futureResult map {
          okResult => Ok("" + okResult)
        } recover {
          case failure => BadRequest(failure.getMessage())
        }
      }
  }

  def calculateAkka() = Action {
    implicit request =>
      val expression = calculateForm.bindFromRequest.get
      Async {
        implicit val timeout = Timeout(5.seconds)
        CalculatorActor.ref ? CalculatorActor.CalculateEvent(expression) map {
          case CalculatorActor.ResultEvent(result) => Ok(result)
          case CalculatorActor.ErrorEvent(e) => BadRequest(e.getMessage())
        }
      }
  }
}