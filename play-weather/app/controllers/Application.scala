package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import models.Location
import models.WeatherService.currentWeather
import play.api.mvc.Action
import play.api.mvc.Controller

object Application extends Controller {
  
  def fetchWeather(location: String) = Action {
    Async {
      currentWeather(Location.withName(location)).map {
        json => Ok(json)        
      }
    }
  }

  def index = Action {
    Ok(views.html.index())
  }
}

