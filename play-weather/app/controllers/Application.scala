package controllers

import play.api.mvc.Controller
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import models.WeatherService._
import models._

object Application extends Controller {

  def fetchWeather(location: String) = Action {

    Async {
      currentWeather(Location.withName(location)).map {
        json => {
          Ok(json)
        }
      }
    }
  }

  def index = Action {
    Ok(views.html.index())
  }

}

