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

package de.knutwalker.forecastio.dispatch.as

import com.ning.http.client.Response
import play.api.libs.json.{Json => PJson, Reads}

//object Spray {
//  import spray.json.{JsonReader, JsonParser}
//
//  class Json[T: JsonReader] extends (Response => T) {
//    def apply(r: Response): T =
//      (dispatch.as.String andThen (s => JsonParser(s).convertTo[T]))(r)
//  }
//
//  object Json {
//    def apply[T: JsonReader]() = new Json[T]
//  }
//}

class Json[T : Reads] extends (Response => T) {
  def apply(r: Response): T =
    (dispatch.as.Bytes andThen (s => PJson.fromJson(PJson.parse(s)).get))(r)
}

object Json {
  def apply[T: Reads]() = new Json[T]
}
