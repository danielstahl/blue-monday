package models

import org.joda.time.DateTime


/**
 *  This is the model for the weather
 */
case class WeatherConditions(location: String, forecasts: Seq[Forecast]) {
}

case class Forecast(from: DateTime, to: DateTime, temperature: Temperature) {
}

case class Temperature(value: Double, unit: TemperatureUnit.Value = TemperatureUnit.celsius) {
}

object TemperatureUnit extends Enumeration {
  val celsius, fahrenheit = Value
}

object Location extends Enumeration {
  var stockholm, karlskoga = Value

  private val locationRestUrls = Map(
    Location.stockholm -> "Stockholm/Stockholm",
    Location.karlskoga -> "Örebro/Karlskoga")

  def restUrl(location: Value): String = {
    locationRestUrls(location)
  }
}
