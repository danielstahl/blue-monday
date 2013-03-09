package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import models.WeatherDataConverter._
import concurrent.Future
import xml.XML
import play.api.libs.json.JsObject
import play.api.cache.Cache

object Application extends Controller {



  //http://stackoverflow.com/questions/7686770/proper-way-to-generate-html-dynamically-with-jquery

  def fetchWeather(location: String) = Action {
    Async {
      fetchWeatherData(Location.withName(location)).map {
        response => {
          Ok((convertWeatherDataToJson(parseWeatherData(response.xml))))
        }
      }

       /*
      Future {
        val weatherXml = xml.XML.loadFile(locationMockUrls(Location.withName(location)))
        val json = convertWeatherDataToJson(parseWeatherData(weatherXml))
        Ok(json)
      }
      */
    }
  }

  def index = Action {
    Ok(views.html.index())
  }

}

