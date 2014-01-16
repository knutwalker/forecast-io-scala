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

package de.knutwalker.forecastio

import com.typesafe.config.ConfigFactory
import de.knutwalker.forecastio.japi.Callback
import org.jboss.netty.handler.codec.http.HttpHeaders
import scala.concurrent.{Await, Future, duration}
import scala.util.{Try, Failure, Success}



/** Create a new API wrapper.
  *
  * A `ForecastIO` as one apply method to asynchronously call the API.
  * Example:
  * {{{
  *   val forecast = ForecastIO("your-api-key-here")
  *   forecast(51.05, 13.37) onSuccess {
  *     case result =>
  *       val temperature = result.currently.map(_.temperature).getOrElse("???")
  *       println("Current temperature is " + temperature)
  *   }
  * }}}
  *
  * The asynchronous execution is backed by [[http://akka.io/ Akka]] and you have to shutdown the `ActorSystem`
  * before terminating the JVM
  * {{{
  *   val forecast = ForecastIO("your-api-key-here")
  *   forecast.shutdown()
  *
  *   // OR
  *   ForecastIO.shutdown()
  * }}}
  *
  * @param apiKey Your Dark Sky API key to make requests.
  * @see https://developer.forecast.io/docs/v2
  */
class ForecastIO(apiKey: String) {

  import ForecastIO._
  import duration._

  /** Performs an API request asynchronously.
    *
    * @param lat   latitude coordinate
    * @param long  longitude coordinate
    * @return a `Future` of the [[de.knutwalker.forecastio.Forecast]] result
    */
  def apply(lat: Double, long: Double): Future[Forecast] =
    request(apiKey, lat, long)

  def apply(lat: Double, long: Double, cb: PartialFunction[Try[Forecast], Unit]): Unit =
    request(apiKey, lat, long, cb)

  /** ''Java API''
    *
    * Performs a request and block to get the result.
    *
    * @param lat   latitude coordinate
    * @param long  longitude coordinate
    * @return a Forecast object from the API call
    * @throws InterruptedException     if the current thread is interrupted while waiting
    * @throws TimeoutException         if after waiting for 1 minute the result is still not ready
    */
  @throws(classOf[Exception])
  def getForecast(lat: Double, long: Double): Forecast =
    Await.result(request(apiKey, lat, long), 1 minute)

  /** ''Java API''
    *
    * Performs an API request asynchronously.
    *
    * @param lat   latitude coordinate
    * @param long  longitude coordinate
    * @param cb    an [[de.knutwalker.forecastio.japi.Callback]], executed at API resolution
    */
  def asyncForecast(lat: Double, long: Double, cb: Callback[Forecast]): Unit =
    apply(lat, long, {
      case Success(forecast) => cb.onSuccess(forecast)
      case Failure(t)        => cb.onFailure(t)
    })
}

object ForecastIO {

  import _root_.dispatch.{Future => _, _}
  import Defaults._

  import de.knutwalker.forecastio.dispatch.as

  val conf = ConfigFactory.load()

  /** base domain for API requests */
  private val BASE_DOMAIN = url("https://api.forecast.io/forecast")

  /** GZip enables HttpClient */
  private val client = Http.configure(_.setCompressionEnabled(true))

  /** constructs URL for an actual request */
  private def svc(apiKey: String, lat: Double, long: Double) =
    (BASE_DOMAIN / apiKey / s"$lat,$long")
      .addHeader(HttpHeaders.Names.ACCEPT_ENCODING, "gzip, deflate")
      .addQueryParameter("units", "si")

  /** executes the request asynchronously on spray-client */
  private def request(apiKey: String, lat: Double, long: Double): Future[Forecast] =
    client(svc(apiKey, lat, long) OK as.Json[Forecast]())

  private def request(apiKey: String, lat: Double, long: Double, cb: PartialFunction[Try[Forecast], Unit]): Unit =
    request(apiKey, lat, long).onComplete(cb)

  private def providedKey = conf.getString("forecast.apikey")


  /** Factory method for creating new [[de.knutwalker.forecastio.ForecastIO]] instances
    *
    * @param apiKey Your Dark Sky API key to make requests.
    */
  def apply(apiKey: String): ForecastIO = new ForecastIO(apiKey)

  /** Factory method for creating new [[de.knutwalker.forecastio.ForecastIO]] instances
    *
    * Read the API key from the config setting `forecast.apikey`
    */
  def apply(): ForecastIO = apply(providedKey)

  /** ''Java API''
    *
    * Factory method for creating new [[de.knutwalker.forecastio.ForecastIO]] instances
    *
    * @param apiKey Your Dark Sky API key to make requests.
    */
  def create(apiKey: String): ForecastIO = apply(apiKey)

  /** ''Java API''
    *
    * Factory method for creating new [[de.knutwalker.forecastio.ForecastIO]] instances
    *
    * Read the API key from the config setting `forecast.apikey`
    */
  def create(): ForecastIO = apply(providedKey)
}
