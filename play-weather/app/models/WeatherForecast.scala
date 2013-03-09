package models

import org.joda.time.DateTime
import collection.mutable.ListBuffer
import xml.{Node, XML}
import play.api.libs.json.{JsObject, Json}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.ws.{WS, Response}
import concurrent.Future
import play.api.cache.Cache

/**
 * Model for a WeatherForecast
 */
case class WeatherConditions(location: String, forcasts: Seq[Forecast]) {

}


case class Forecast(from: DateTime, to: DateTime, temperature: Temperature) {

}

case class Temperature(value: Double, unit: TemperatureUnit.Value = TemperatureUnit.celsius) {

}

object TemperatureUnit extends Enumeration {
  val celsius, fahrenheit = Value
}



object WeatherDataConverter {
  def parseWeatherData(weatherXml: Node):WeatherConditions = {
    WeatherConditions(
      (weatherXml \\ "location" \ "name").text,
      (weatherXml \\ "forecast" \ "tabular" \ "time").map {
        time => Forecast(
          DateTime.parse((time \ "@from").text),
          DateTime.parse((time \ "@to").text),
          Temperature((time \ "temperature" \ "@value").text.toDouble)
        )
      })
  }

  def convertWeatherDataToJson(condition: WeatherConditions):JsObject = {
    Json.obj(
      "weatherConditions" -> Json.obj(
        "location" -> condition.location,
        "conditions" -> Json.arr(
          condition.forcasts.map(
            cond => {
              Json.obj(
              "period" -> formatPeriod(cond.from, cond.to),
              "temperature" -> cond.temperature.value
              )
            }
          )
        )
      )
    )
  }

  def formatPeriod(from: DateTime, to: DateTime):String = {
    formatDate(from, new DateTime()) + " - " + formatDate(to, from)
  }

  def formatDate(theDate: DateTime, compareDate: DateTime):String = {
    if(theDate.dayOfYear().get() == compareDate.dayOfYear().get()) {
      theDate.toString("HH")
    } else {
      theDate.toString("EEEE HH")
    }
  }

  object Location extends Enumeration() {
    var stockholm, karlskoga = Value
  }

  var locationRestUrls = Map(
    Location.stockholm -> "Stockholm/Stockholm",
    Location.karlskoga -> "Örebro/Karlskoga"
  )

  var locationMockUrls = Map(
    Location.stockholm -> "/Users/danielstahl/Downloads/forecast.xml",
    Location.karlskoga -> "/Users/danielstahl/Downloads/karlskoga-forecast.xml"
  )

  def fetchWeatherData(location: Location.Value):Future[Response] = {
    WS.url("http://www.yr.no/place/Sweden/" + locationRestUrls(location) + "/forecast.xml").get()
  }
}