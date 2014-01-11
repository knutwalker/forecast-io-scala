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

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{Try, Failure, Success}
import spray.can.Http
import spray.util._
import de.knutwalker.forecastio.japi.Callback
import akka.event.Logging

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
  // the ExecutionService for the Futures from the Java-API

  /** Performs an API request asynchronously.
    *
    * @param lat   latitude coordinate
    * @param long  longitude coordinate
    * @return a `Future` of the [[de.knutwalker.forecastio.Forecast]] result
    */
  def apply(lat: Double, long: Double): Future[Forecast] =
    request(apiKey, lat, long)

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
    request(apiKey, lat, long) await (1 minute)

  /** ''Java API''
    *
    * Performs an API request asynchronously.
    *
    * @param lat   latitude coordinate
    * @param long  longitude coordinate
    * @param cb    an [[de.knutwalker.forecastio.japi.Callback]], executed at API resolution
    */
  def asyncForecast(lat: Double, long: Double, cb: Callback[Forecast]): Unit =
    apply(lat, long) onComplete {
      case Success(forecast) => cb.onSuccess(forecast)
      case Failure(t)        =>
        log.error(t, "API call unsuccessful")
        cb.onFailure(t)
    }

  /** Shuts down the underlying `ActorSystem` */
  def shutdown(): Unit = ForecastIO.shutdown()

}

object ForecastIO {

  import spray.client.pipelining._
  import spray.http._
  import spray.httpx.SprayJsonSupport._

  implicit private val system = ActorSystem("forecastio")
  implicit val executionContext = system.dispatcher

  /** base domain for API requests */
  private val BASE_DOMAIN = Uri("https://api.forecast.io/forecast")

  /** constructs URL for an actual request */
  private def url(apiKey: String, lat: Double, long: Double) =
    BASE_DOMAIN.withPath(BASE_DOMAIN.path / apiKey / s"$lat,$long").withQuery("units" -> "si")

  /** read returned JSON into a Forecast POJO */
  private val pipeline: HttpRequest => Future[Forecast] = sendReceive ~> unmarshal[Forecast]

  /** executes the request asynchronously on spray-client */
  private def request(apiKey: String, lat: Double, long: Double): Future[Forecast] = pipeline {
    Get(url(apiKey, lat, long))
  }

  private def providedKey = system.settings.config.getString("forecast.apikey")


  /** The logging instance */
  val log = Logging(system, getClass)

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


  /** Shuts down the actor system and closes all pending connections. */
  def shutdown(): Unit = {
    IO(Http).ask(Http.CloseAll)(1 second).await
    system.shutdown()
  }
}
