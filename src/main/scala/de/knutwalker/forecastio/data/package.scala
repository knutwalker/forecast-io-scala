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

/**
 * Data POJOs
 * @see https://developer.forecast.io/docs/v2
 */
package de.knutwalker.forecastio.data

import scala.beans.BeanProperty


case class DailyDataPoint(@BeanProperty sunriseTime: Long,
                          @BeanProperty sunsetTime: Long)
case class DailyDataBlock(@BeanProperty summary: String,
                          @BeanProperty icon: String,
                                        data: Vector[DailyDataPoint]){
  /** ''Java API'' */
  def getDataPoints: Array[DailyDataPoint] = data.toArray
}


case class HourlyDataPoint(@BeanProperty summary: String,
                           @BeanProperty icon: String,
                           @BeanProperty temperature: Double,
                           @BeanProperty apparentTemperature: Double,
                           @BeanProperty dewPoint: Double,
                           @BeanProperty humidity: Double,
                           @BeanProperty windSpeed: Double,
                           @BeanProperty windBearing: Int)
case class HourlyDataBlock(@BeanProperty summary: String,
                           @BeanProperty icon: String,
                                         data: Vector[HourlyDataPoint]){
  /** ''Java API'' */
  def getDataPoints: Array[HourlyDataPoint] = data.toArray
}


case class MinutelyDataPoint(@BeanProperty time: Long,
                             @BeanProperty precipIntensity: Double,
                             @BeanProperty precipProbability: Double,
                             @BeanProperty precipIntensityError: Double,
                             @BeanProperty precipType: String)
case class MinutelyDataBlock(@BeanProperty summary: String,
                             @BeanProperty icon: String,
                                           data: Vector[MinutelyDataPoint]){
  /** ''Java API'' */
  def getDataPoints: Array[MinutelyDataPoint] = data.toArray
}



object DataProtocol {
//  import spray.json._
//  import DefaultJsonProtocol._

//  implicit lazy val dailyDataPointFormat = jsonFormat2(DailyDataPoint)
//  implicit lazy val dailyDataBlockFormat = jsonFormat3(DailyDataBlock)
//
//  implicit lazy val hourlyDataPointFormat = jsonFormat8(HourlyDataPoint)
//  implicit lazy val hourlyDataBlockFormat = jsonFormat3(HourlyDataBlock)
//
//  implicit lazy val minutelyDataPointFormat = jsonFormat5(MinutelyDataPoint)
//  implicit lazy val minutelyDataBlockFormat = jsonFormat3(MinutelyDataBlock)


  import play.api.libs.json.Json

  implicit lazy val dailyDataPointReads = Json.reads[DailyDataPoint]
  implicit lazy val dailyDataBlockReads = Json.reads[DailyDataBlock]

  implicit lazy val hourlyDataPointReads = Json.reads[HourlyDataPoint]
  implicit lazy val hourlyDataBlockReads = Json.reads[HourlyDataBlock]

  implicit lazy val minutelyDataPointReads = Json.reads[MinutelyDataPoint]
  implicit lazy val minutelyDataBlockReads = Json.reads[MinutelyDataBlock]
}

