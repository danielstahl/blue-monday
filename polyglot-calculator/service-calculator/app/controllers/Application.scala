package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.Future
import models.Calculator
import play.api.libs.concurrent.Execution.Implicits._
import play.api.http.HeaderNames._

object Application extends Controller {

  def index = Action {
    Ok("Hello world.")
  }

  val calculateForm = Form(
    "expression" -> text)

  def calculate() = AccessControl(internalCalculate)
  
  private def internalCalculate() = Action {
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

  case class AccessControl[A](action: Action[A]) extends Action[A] {    
    def apply(request: Request[A]): Result = {
      val result = action(request)
      request.headers.get(ORIGIN) match {
        case Some(origin) => result.withHeaders(
        		ACCESS_CONTROL_ALLOW_ORIGIN -> origin)
        case None => result
      }
      
      
      
      action(request).withHeaders(
        ACCESS_CONTROL_ALLOW_ORIGIN -> request.headers.get(ORIGIN).getOrElse("*"))
    }

    lazy val parser = action.parser
  }
}
