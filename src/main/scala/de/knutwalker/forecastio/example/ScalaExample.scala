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

import scala.util.{Failure, Success}
import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

import de.knutwalker.forecastio.ForecastIO


object ScalaExample extends App {
  import ForecastIO.executionContext

  val forecastIO = ForecastIO("your-api-key-here")
  val forecast   = forecastIO(51.05, 13.37)

  forecast onComplete {
    case Success(result) =>

      println(s"Current temperature is ${result.currently.map(_.temperature).getOrElse("???")}")
      println(s"Apparent temperature is ${result.currently.map(_.apparentTemperature).getOrElse("???")}")

      val isoFormat = new SimpleDateFormat("HH:mm:ss")
      isoFormat.setTimeZone(TimeZone.getTimeZone(result.timezone))

      result.daily.flatMap(_.data.headOption) foreach { today =>

        val sunriseDate = new Date(today.getSunriseTime * 1000L)
        val sunsetDate = new Date(today.getSunsetTime * 1000L)

        println(s"Sunrise today was at ${isoFormat.format(sunriseDate)}")
        println(s"Sunrise today will be at ${isoFormat.format(sunsetDate)}")
      }

      ForecastIO.shutdown()

    case Failure(t) =>
      ForecastIO.shutdown()

  }
}
