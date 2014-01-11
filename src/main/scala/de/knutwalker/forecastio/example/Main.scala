/*
 * Copyright (c) 2014 Paul Horn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.knutwalker.forecastio.example


import de.knutwalker.forecastio._
import de.knutwalker.forecastio.data._

import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

import scala.util.{Failure, Try, Success}

object Main extends App {

  import ForecastIO.executionContext

  val isoFormat = new SimpleDateFormat("HH:mm:ss")

  val lat  = Try(args(0)).flatMap(s => Try(s.toDouble)).toOption.getOrElse(51.05)
  val long = Try(args(1)).flatMap(s => Try(s.toDouble)).toOption.getOrElse(13.37)

  val forecast   = ForecastIO()(lat, long)

  forecast onComplete {
    case Success(Forecast(latitude, longitude, timezone, _, Some(HourlyDataPoint(summary, _, temp, apparent, _, humidity, wind, _)), _, _, Some(DailyDataBlock(_, _, Vector(DailyDataPoint(sunrise, sunset), _*))))) =>

      isoFormat.setTimeZone(TimeZone.getTimeZone(timezone))
      val sunriseDate = new Date(sunrise * 1000L)
      val sunsetDate = new Date(sunset * 1000L)

      println(
        s"""$summary.
          |Lat|Long: $latitude,$longitude
          |temperature: $temp ºC, apparently: $apparent ºC
          |humidity: $humidity %
          |wind-speed: $wind
          |sunrise: ${isoFormat.format(sunriseDate)}
          |sunset: ${isoFormat.format(sunsetDate)}
        """.stripMargin
      )

      ForecastIO.shutdown()

    case Failure(t) =>
      println(t.getMessage)
      ForecastIO.shutdown()

    case otherwise => ForecastIO.shutdown()
  }

}
