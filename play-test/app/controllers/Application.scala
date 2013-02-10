package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.data.Form
import play.api.data.Forms._
import models.Calculator


object Application extends Controller {
	def index = Action {
	  Ok(views.html.index(calculateForm, "-"))
	}
		
	val calculateForm = Form(
			"expression" -> text
	)
	
	def calculate = Action { implicit request =>
	  val expr = calculateForm.bindFromRequest.get;
	  Ok(views.html.index(calculateForm,  expr + " = " + Calculator.apply(expr)))
	}
}