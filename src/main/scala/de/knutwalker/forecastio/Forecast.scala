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

import de.knutwalker.forecastio.data._
import scala.beans.BeanProperty


case class Forecast(@BeanProperty latitude: Double,
                    @BeanProperty longitude: Double,
                    @BeanProperty timezone: String,
                    @BeanProperty offset: Int,
                    currently: Option[HourlyDataPoint],
                    minutely: Option[MinutelyDataBlock],
                    hourly: Option[HourlyDataBlock],
                    daily: Option[DailyDataBlock]) {

  /** ''Java API''
    * @return an [[de.knutwalker.forecastio.data.HourlyDataPoint]] or `null`
    */
  def getCurrently: HourlyDataPoint = currently.orNull

  /** ''Java API''
    * @return an [[de.knutwalker.forecastio.data.MinutelyDataBlock]] or `null`
    */
  def getMinutely: MinutelyDataBlock = minutely.orNull

  /** ''Java API''
    * @return an [[de.knutwalker.forecastio.data.HourlyDataBlock]] or `null`
    */
  def getHourly: HourlyDataBlock = hourly.orNull

  /** ''Java API''
    * @return an [[de.knutwalker.forecastio.data.DailyDataBlock]] or `null`
    */
  def getDaily: DailyDataBlock = daily.orNull
}

case object Forecast {
//  import spray.json._
  import DataProtocol._
//  import DefaultJsonProtocol._

  import play.api.libs.json.Json

//  implicit lazy val forecastFormat = jsonFormat8(Forecast.apply)

  implicit lazy val forecastReads = Json.reads[Forecast]
}
