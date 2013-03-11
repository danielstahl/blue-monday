package models

import org.joda.time.DateTime
import xml.Node
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.WS
import scala.concurrent._
import play.api.cache.Cache
import scala.xml.Elem
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current
import scala.Some
import WeatherDataRetriever._
import WeatherDataConverter._

/**
 * Model for a WeatherForecast
 */


object WeatherService {
  def currentWeather(location: Location.Value): Future[JsObject] = {
    fetchWeatherData(location).map {
      response => {
        convertWeatherDataToJson(parseWeatherData(response))
      }
    }
  }
}

object WeatherDataRetriever {
  private val CACHE_TIME = 60 * 10

  def fetchWeatherData(location: Location.Value): Future[Elem] = {
    val forecast: Option[Elem] = Cache.getAs[Elem]("forcast" + location.toString())
    forecast match {
      case None => {
        WS.url("http://www.yr.no/place/Sweden/" + Location.restUrl(location) + "/forecast.xml").get().map {
          response => {
            Cache.set("forcast" + location.toString(), response.xml, CACHE_TIME)
            response.xml
          }
        }
      }
      case Some(x) => {
        future {
          x
        }
      }
    }
  }
}

object WeatherDataConverter {
  def parseWeatherData(weatherXml: Node): WeatherConditions = {
    WeatherConditions(
      (weatherXml \\ "location" \ "name").text,
      (weatherXml \\ "forecast" \ "tabular" \ "time").map {
        time =>
          Forecast(
            DateTime.parse((time \ "@from").text),
            DateTime.parse((time \ "@to").text),
            Temperature((time \ "temperature" \ "@value").text.toDouble))
      })
  }

  def convertWeatherDataToJson(condition: WeatherConditions): JsObject = {
    Json.obj(
      "weatherConditions" -> Json.obj(
        "location" -> condition.location,
        "conditions" -> Json.arr(
          condition.forecasts.map(
            cond => {
              Json.obj(
                "period" -> formatPeriod(cond.from, cond.to),
                "time" -> formatDate(cond.from, new DateTime()),
                "temperature" -> cond.temperature.value
              )
            }
          )
        )
      )
    )
  }

  def formatPeriod(from: DateTime, to: DateTime): String = {
    formatDate(from, new DateTime()) + " - " + formatDate(to, from)
  }

  def formatDate(theDate: DateTime, compareDate: DateTime): String = {
    if (theDate.dayOfYear().get() == compareDate.dayOfYear().get()) {
      theDate.toString("HH.mm")
    } else {
      theDate.toString("EEEE HH.mm")
    }
  }
}




